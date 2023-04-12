#include <ESP8266WiFi.h>
#include <NewPing.h>


 const char* SSID ="Hisham_4G"; //the HotSpot that is going to be connected to
 const char* password="1320k1320"; //the password for it
 const uint16_t port = 28002; //the port specified by the server
 const char* host = "192.168.100.20"; //server IP address

WiFiClient client;
#define red D0
#define green D1
#define blue D2
#define trig D6
#define echo D5
NewPing sensor(trig,echo, 400);//triger then echo pin then the maxiumm distence
     

     void setup() {
    Serial.begin(9600);
    
    WiFi.mode(WIFI_STA);
    WiFi.begin(SSID,password);

    connect_toWiFi();
    
    //making a new client and connecting to the host and the designated port
    WiFiClient client;
      while (!client.connect(host, port)) {
      Serial.println("Connection to Server failed");
      }
      rgb(0,255,0);
  
}
bool isParked = true;
void loop() {
    //checking if connected to wifi if not go to connect_toWiFi function
    if(WiFi.status() != WL_CONNECTED || !client.connected())
    connect_toWiFi();
    
  //the sensor conditionals
  //1st -> if there is somthing under 60cm light LED RED
  //2nd -> if there is nothing under 60cm light LED GREEN
  if(sensor.ping_cm()<60 && isParked == true){
  rgb(255,0,0);
  isParked = false;
  client.write(1);
  delay(100);
  delay(100);
  } else if(sensor.ping_cm() >61 && isParked == false){
  rgb(0,255,0);
  isParked = true;
  client.write(2);
  delay(100);
  }delay(100);
  
  

}

void rgb(int i, int j, int k){
  analogWrite(red, i);
  analogWrite(green,j);
  analogWrite(blue,k);
  
  }


   void connect_toWiFi(){
    Serial.print("WiFi connecting to  ");
    Serial.print(SSID);
    Serial.println("Connecting");
    while(WiFi.status() != WL_CONNECTED){
      rgb(0,0,255);
      delay(500);
      rgb(0,0,0);
      delay(200);
    }

    rgb(0,255,0);
    delay(5000);
    rgb(0,0,0);
    

    Serial.print("\nWiFi connected Success!");
    Serial.println("NodeMCI IP Address");
    Serial.println(WiFi.localIP());

    while (!client.connect(host, port)) {
      rgb(128,0,128);
      delay(1000);
      rgb(0,0,0);
      }

    }
      
  
