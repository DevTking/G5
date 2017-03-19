package g5.org.g5;

import android.app.Notification;
import android.bluetooth.*;
import android.os.SystemClock;
import g5.org.g5.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.UUID;

class BtGattCallback extends BluetoothGattCallback {
    private static Logger log = LoggerFactory.getLogger(BtGattCallback.class);
    private EnumMap<S, BluetoothGattCharacteristic> d = new EnumMap<S, BluetoothGattCharacteristic>(S.class);

    @Override
    public void onConnectionStateChange(final BluetoothGatt gatt, int reasonCode, int newState) {
        log.debug("onConnectionStateChange " + newState + " reasonCode:"+reasonCode);
        if (newState == 2) {
            MainApp.setupAlarm();
            Notification notification = MainApp.instance().m_notification;
            notification.when = System.currentTimeMillis();
            MainApp.instance().mNotificationManager.notify(1, notification);

            if (gatt.getServices().isEmpty()) {
				log.debug("getServices empty");
				MainApp.handler.post(new Runnable() {
					@Override
					public void run() {
						gatt.discoverServices();
					}
				});
			} else {
                log.debug("getServices not empty");
            	this.onServicesDiscovered(gatt,0);
			}

        } else {
            //gatt.close();
            if(MainApp.instance().wl.isHeld()) {
                MainApp.instance().wl.release();
            }
        }
    }

    enum S {
        AuthenticationControlPoint,
        CgmControlPoint,
        Synchronization,
        Exchange
    }


	@Override
	public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
		byte[] value = characteristic.getValue();

		log.debug("onCharacteristicChanged " + aC.getName(characteristic.getUuid()) + " " +  MainActivity.toHexString(value));


		if(BtGattCallback.this.d.get(S.CgmControlPoint).getUuid().equals(characteristic.getUuid())) {
			if (value[0] == (byte)0x2f) {
				SensorRxMessage sensorRx = new SensorRxMessage(value);
				log.debug("sensor "+sensorRx.timestamp + " "+sensorRx.filtered + " "+sensorRx.unfiltered);

				sendDisconnect(gatt);
			}
		}
	}

	@Override
	public void onDescriptorWrite(final BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
		log.debug("onDescriptorWrite "+aC.getName(descriptor.getCharacteristic().getUuid()) + status);

		MainApp.handler.post(new Runnable() {
			@Override
			public void run() {
				SensorTxMessage sensorTx = new SensorTxMessage();
				BluetoothGattCharacteristic charCgmControlPoint = BtGattCallback.this.d.get(S.CgmControlPoint);
				charCgmControlPoint.setValue(sensorTx.byteSequence);
				gatt.writeCharacteristic(charCgmControlPoint);
			}
		});

	}

	@Override
    public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
        log.debug("onCharacteristicRead " + aC.getName(characteristic.getUuid()));
        final byte[] value = characteristic.getValue();
        log.debug("onCharacteristicRead " + MainActivity.toHexString(value));

        if (value.length == 0) {
            log.warn( "onCharacteristicRead 0 bytes");
            return;
        }


        switch (value[0]) {
			case 7:
				// bond
				break;
            case 5:
				AuthStatusRxMessage authStatus = new AuthStatusRxMessage(value);

				MainApp.handler.post(new Runnable() {
					@Override
					public void run() {
						BluetoothGattCharacteristic charCgmControlPoint = BtGattCallback.this.d.get(S.CgmControlPoint);//.setValue(authRequest.byteSequence);
						gatt.setCharacteristicNotification(charCgmControlPoint, true);
						final BluetoothGattDescriptor descriptor = charCgmControlPoint.getDescriptor(
							UUID.fromString("00002902-0000-1000-8000-00805F9B34FB"));
						descriptor.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
						gatt.writeDescriptor(descriptor);

						//sendDisconnect(gatt);
					}
				});

				break;
			case 3:
				sendResponseToChallenge(gatt, characteristic);
				break;

            case 0:
                sendAuth(gatt);
				break;
			case (byte) 0xff:
				//sendUnboundReq(gatt);
				sendAuth(gatt);
				break;
        }


    }

	private void sendResponseToChallenge(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
		MainApp.handler.post(new Runnable() {
			@Override
			public void run() {
				log.debug("sendResponseToChallenge " );
				AuthChallengeRxMessage authChallenge = new AuthChallengeRxMessage(characteristic.getValue());

				byte[] challengeHash = authChallenge.calculateHash(authChallenge.challenge);

				if (challengeHash != null) {

					AuthChallengeTxMessage authChallengeTx = new AuthChallengeTxMessage(challengeHash);

					characteristic.setValue(authChallengeTx.byteSequence);
					gatt.writeCharacteristic(characteristic);

				}
			}});
	}

	private void sendAuth(final BluetoothGatt gatt) {
		MainApp.handler.post(new Runnable() {
			@Override
			public void run() {
				log.debug("sendAuth " );
				AuthRequestTxMessage authRequest = new AuthRequestTxMessage();
				BtGattCallback.this.d.get(S.AuthenticationControlPoint).setValue(authRequest.byteSequence);
				gatt.writeCharacteristic(BtGattCallback.this.d.get(S.AuthenticationControlPoint));
			}
		});
	}


	private void sendUnboundReq(final BluetoothGatt gatt) {
		MainApp.handler.post(new Runnable() {
			@Override
			public void run() {
				log.debug("sendUnboundReq " );
				UnbondRequestTxMessage authRequest = new UnbondRequestTxMessage();
				BtGattCallback.this.d.get(S.AuthenticationControlPoint).setValue(authRequest.byteSequence);
				gatt.writeCharacteristic(BtGattCallback.this.d.get(S.AuthenticationControlPoint));
			}
		});
	}


	@Override
	public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, int status) {
		log.debug("onCharacteristicWrite " + aC.getName(characteristic.getUuid()) + " status:"+status);
		if(BtGattCallback.this.d.get(S.AuthenticationControlPoint).getUuid().equals(characteristic.getUuid())) {
			MainApp.handler.post(new Runnable() {
				@Override
				public void run() {
					gatt.readCharacteristic(characteristic);
				}
			});
		}
	}

	private void sendDisconnect(BluetoothGatt gatt) {
		log.debug("sendDisconnect ");
        byte[] disco = new byte[] { 0x09};
        this.d.get(S.CgmControlPoint).setValue(disco);
        gatt.writeCharacteristic(this.d.get(S.CgmControlPoint));
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int status) {
        log.debug("onServicesDiscovered " + status);

        BluetoothGattService service = bluetoothGatt.getService(aC.DexcomCgmServiceUuid);
        if (service != null) {
            this.d.put(S.AuthenticationControlPoint, service.getCharacteristic(aC.AuthenticationControlPointCharUuid));
            this.d.put(S.CgmControlPoint, service.getCharacteristic(aC.CgmControlPointCharUuid));
            this.d.put(S.Synchronization, service.getCharacteristic(aC.SynchronizationCharUuid));
            this.d.put(S.Exchange, service.getCharacteristic(aC.ExchangeCharUuid));
        }
        bluetoothGatt.readRemoteRssi();
        bluetoothGatt.readCharacteristic(this.d.get(S.AuthenticationControlPoint));

        //bluetoothGatt.disconnect();
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        log.debug("onReadRemoteRssi " + status + " " + rssi);
    }

    @Override
    public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
        log.debug("BluetoothGatt " + status + " " + mtu);
    }
}
