
  MCLib库使用方式
============
 1.USB转串口方式：
--------------
 ### 第一步：
编写类继承AbstractUsbSerial并实现其方法。
### 包含的方法有：
### 1.onSubmitRunnable
用于提交线程方法 比如循坏发送数据的线程等。
### 2.onSerialConfig
返回串口配置实体 主要用于配置串口的监听器 波特率以及USB的VendorId和
ProductId 例如：
 ```
  protected SerialConfig onSerialConfig() {
         return new SerialConfig.Builder()
                 .setSerialListener(this)
                 .setBaudRate(9600)
                 .setVendorId(1659)
                 .setProductId(8963)
                 .setContext(App.application)
                 .builder();
 
     }
 ```
 
### 3 ISerialListener 接口
###### onSerialInitComplete 串口加载完成的回调
###### onSerialError        串口出错的回调
### 4 onTerminationReceive 是否终止接收
### 5.onTerminationSend    是否终止发送
## 第二步：
 在Android工程res目录下新建xml文件夹 文件名device_filer,
 内容格式如下图，其中就包含你USB的 vendor-id 和 product-id
 ```
 <?xml version="1.0" encoding="utf-8"?>
 <resources>
     <!-- 0x0403 / 0x6001: FTDI FT232R UART -->
     <usb-device vendor-id="1027" product-id="24577" />
     <!-- 0x0403 / 0x6015: FTDI FT231X -->
     <usb-device vendor-id="1027" product-id="24597" />
     <!-- 0x2341 / Arduino -->
     <usb-device vendor-id="9025" />
     <!-- 0x16C0 / 0x0483: Teensyduino  -->
     <usb-device vendor-id="5824" product-id="1155" />
     <!-- 0x10C4 / 0xEA60: CP210x UART Bridge -->
     <usb-device vendor-id="4292" product-id="60000" />
     <!-- 0x067B / 0x2303: Prolific PL2303 -->
     <usb-device vendor-id="1659" product-id="8963" />
     <!-- 0x1a86 / 0x7523: Qinheng CH340 -->
     <usb-device vendor-id="6790" product-id="29987" />
 </resources>
 ```
## 第三步：
  在你Android工程的清单文件里添加权限等 如图：
  ```
 <uses-feature android:name="android.hardware.usb.host" />
         <application
             android:allowBackup="true"
             android:icon="@mipmap/ic_launcher"
             android:name=".App"
             android:label="@string/app_name"
             android:roundIcon="@mipmap/ic_launcher_round"
             android:supportsRtl="true"
             android:theme="@style/AppTheme">
             <activity android:name=".MainActivity">
                 <intent-filter>
                     <action android:name="android.intent.action.MAIN" />
                     <category android:name="android.intent.category.LAUNCHER" />
                 </intent-filter>
                 <intent-filter>
                     <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
                 </intent-filter>
                 <meta-data
                     android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
                     android:resource="@xml/device_filter" />
             </activity>
         </application>
 ```
## 2.非USB转串口方式：
方法和USB转串口相同，但是继承AbstractSerial 返回的SerialConfig配置不同 如
=======
```
 protected SerialConfig onSerialConfig() {
        return new SerialConfig.Builder()
                .setSerialListener(this)
                .setBaudRate(9600)
                .setVendorId(1659)
                .setProductId(8963)
                .setContext(App.application)
                .builder();

    }
```

### 3 ISerialListener 接口
###### onSerialInitComplete 串口加载完成的回调
###### onSerialError        串口出错的回调
### 4 onTerminationReceive 是否终止接收 
### 5.onTerminationSend    是否终止发送 
### 第二步： 
在Android工程res目录下新建xml文件夹 文件名device_filer,
内容格式如下图，其中就包含你USB的 vendor-id 和 product-id
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- 0x0403 / 0x6001: FTDI FT232R UART -->
    <usb-device vendor-id="1027" product-id="24577" />
    <!-- 0x0403 / 0x6015: FTDI FT231X -->
    <usb-device vendor-id="1027" product-id="24597" />
    <!-- 0x2341 / Arduino -->
    <usb-device vendor-id="9025" />
    <!-- 0x16C0 / 0x0483: Teensyduino  -->
    <usb-device vendor-id="5824" product-id="1155" />
    <!-- 0x10C4 / 0xEA60: CP210x UART Bridge -->
    <usb-device vendor-id="4292" product-id="60000" />
    <!-- 0x067B / 0x2303: Prolific PL2303 -->
    <usb-device vendor-id="1659" product-id="8963" />
    <!-- 0x1a86 / 0x7523: Qinheng CH340 -->
    <usb-device vendor-id="6790" product-id="29987" />
</resources>
```
## 第三步：
在你Android工程的清单文件里添加权限等 如图：
 ```
<uses-feature android:name="android.hardware.usb.host" />
        <application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:name=".App"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">
            <activity android:name=".MainActivity">
                <intent-filter>
                    <action android:name="android.intent.action.MAIN" />
                    <category android:name="android.intent.category.LAUNCHER" />
                </intent-filter>
                <intent-filter>
                    <action android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED" />
                </intent-filter>
                <meta-data
                    android:name="android.hardware.usb.action.USB_DEVICE_ATTACHED"
                    android:resource="@xml/device_filter" />
            </activity>
        </application>
```
## 2.非USB转串口方式：
方法类型和USB转串口相同，但是继承AbstractSerial 返回的SerialConfig配置不同 如图
>>>>>>> 5ccb62f2d495bad1b2c7664089d3d0a759c34777
```
   protected SerialConfig onSerialConfig() {
        return new SerialConfig.Builder()
                .setBaudRate(9600)
                .setPort("/dev/ttyO3")
                .setSerialListener(this)
                .builder();

    }
 ```
同时还需重写onSendMessage用于发送数据 不需要执行USB转串口的第二步和第三步。

### ###使用方式 #根 build.gradle添加
  ```
  allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
  ```
### #APP的build.gradle添加 Tag 当前版本
   ```
   dependencies { 
           implementation 'com.github.WuSG2016:McLibrary:Tag'
 }
   ```
