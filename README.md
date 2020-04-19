# Internship-5GCN
Implementation of RESTful Web Services between 5G Control Plane Nodes(AMF,NRF,SMF,UDM)

# Project Descripotion
It is an implementation of Restful Web Services between some of the 5G Control Network Functions
(AMF,SMF,UDM,NRF) according to specifications 23.501, 23.502, 23.503, 29.501, 29.502, 
29.503, 29.510 defined by 3GPP group. The specifications can be found at https://www.3gpp.org/specifications.

# Detailed Information
5G CN is a Service Oriented Architecture and it is specified to be RESTful. With this
The services written using java version 1.8. There are 4 different network functions implemented in this project which are namely AMF, SMF, UDM and NRF. Each network function receives and sends request using HTTP2 protocol which is selected to be used in 5G Core Network. In each function a HTTP2 servlet created using Jetty and Jersey. Then request are sent using okhttpClient. The project built using maven. The possible scenarios between Network Functions can be seen in the images below and more detailed info can be found in the internship report.

# Technologies Used
Architecture : SBA(Service Based Architecture)<br/>
Web Service Design : REST<br/>
Project Language : Java<br/>
JAX-RS library<br/>
To send request(Client): okhttp<br/>
To handle incoming requests(Server): Jetty & Jersey<br/>
Protocol : Http2<br/>
Data type: JSON<br/>
Endpoints: Defined in 3GPP specifications<br/>
Object models : Defined in 3GPP specifications<br/>
Status codes, loads, headers : Fefined in 3GPP specifications<br/>
Tests: Junit<br/>

# Running project files
The jar files can only be run by adding the apln.jar file to the Xbootclasspath.<br/> Therefore you need to download the alpn.jar file as well.<br/>
An example command is below <br/>
java -Xbootclasspath/p:/C:/Users/{$userName}/Downloads/alpn-boot-8.1.12.v20180117.jar -jar target/UDM.jar

# Implemented Scenarios
## NRF Services
![NRF Service1](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/NRF%20Serviecs/NRF%20Services-1.png? "NRF Service")
![NRF Service2](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/NRF%20Serviecs/NRF%20Services-2.png? "NRF Service2")
![NRF Service3](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/NRF%20Serviecs/NRF%20Services-3.png? "NRF Service3")

## UDM Services
![UDM Service1](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/UDM%20Services/UDM%20Services-1.png? "UDM Service")
![UDM Service2](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/UDM%20Services/UDM%20Services-2.png? "UDM Service")

## Example Scenarios
### PDU Session Creation
![Scenario 1](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/SMF%20Services/PDU%20Session%20Creation-1.png?  "Scenario 1")
### AMF Registration
![Scenario 2](https://github.com/bubblecounter/Internship-5GCN/blob/master/5GCN%20Schemes/UDM%20Services/AMFRegistration-1.png?  "Scenario 2")
