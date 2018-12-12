[![Build Status](https://travis-ci.com/tehokang/sedec-2.0-java.svg?token=tukzKYpBaAzTzcPeGfcg&branch=master)](https://travis-ci.com/tehokang/sedec-2.0-java)

# Table of Contents
* [Introduction](#Introduction)
	* [DVB](#DVB)
	* [ARIB](#ARIB)
		* [TLV](#TLV)
			* [Network Time Protocol Data in IPv4, IPv6 Packet](#Network-Time-Protocol-Data-in-IPv4,-IPv6-Packet)
			* [TLV-SI in Signalling Packet](#TLV-SI-in-Signalling-Packet)
			* [MMTP as known as MPEG Media Transport Protocol in Compressed IP Packet](#MMTP-as-known-as-MPEG-Media-Transport-Protocol-in-Compressed-IP-Packet)
				* [MPU-MFU](#MPU-MFU)
			* [Signalling Message](#Signalling-Message)
				* [Tables](#Tables)
* [Architecture](#Architecture)
	* [Package Tree](#Package-Tree)
* [How To Build](#How-To-Build)
* [How To Run](#How-To-Run)
* [To Do](#To-Do)


# Introduction

SEDEC is to easily make decoder(and even encoder) of tables of DVB, ARIB as MPEG sections and whatever inherited from ISO-13818. SEDEC stand for <font color="red">**SE**</font>ction <font color="red">**DEC**</font>oder. Here whatever mean whoever could put new sections into SEDEC with easy steps. 

You could get started to make a kind of sections to decode like following easy step, <br>
* Create table based on base/Table in case of DVB, arib/Table in case of ARIB <br>
* Add parse routine in their constructor, basically the constructor get binary data starting table_id as known as beginning of the section.<br>
* Add getter function if you want to provide interfaces. <br>
* Add print function if you want to show the value of fields to be decoded. <br>
* Finally you can add the table into factory of tables <br>

Currently SEDEC-2.0-java is including sections like below, it can be decoded all of them.

## DVB 

Tables as known as section 
* Program Association Table AKA PAT  <br>
* Program Map Table (PMT) <br>
* Conditional Access Table (CAT) <br>
* Application Information Table (AIT) <br>
* Bouquet Association Table (BAT) <br>
* Discontinuity Information Table (DIT) <br>
* Event Information Table (EIT) <br>
* Network Information Table (NIT) <br>
* Running Status Table (RST) <br>
* Selection Information Table (SIT) <br>
* Service Description Table (SDT) <br>
* Stuffing Table (ST) <br>
* Time Date Table (TDT) <br>
* Time Offset Table (TOT) <br>

Descriptors :
* Application Descriptor <br>
* Application Name Descriptor <br>
* Application Recording Descriptor <br>
* Application Usage Descriptor <br>
* Connection Requirement Descriptor <br>
* Parental Rating Descriptor <br>
* Simple Application Boundary Descriptor <br>
* Simple Application Location Descriptor <br>
* Transport Protocol Descriptor <br>


## ARIB

### TLV Container

* IPv4 Packet <br>
* IPv6 Packet <br>
* Signalling Packet <br>
* Compressed IP Packet <br>
* Null Packet <br>

#### Network Time Protocol Data in IPv4, IPv6 Packet

* Network Time Protocol Data

#### TLV-SI in Signalling Packet

Tables :
* Address Map Table (AMT)
* TLV_Network Information Table (TLV-NIT)

Descriptors:
* Channel Bonding Cable Delivery System Descriptor
* Network Name Descriptor
* Remote Control Key Descriptor
* Sarelite Delivery System Descriptor
* Service List Descriptor
* System Management Descriptor

#### MMTP as known as MPEG Media Transport Protocol in Compressed IP Packet

* MMTP Packet Header <br>
* MMTP Packet Payload <br>
    * MPU-MFU <br>
        * Video <br>
        * Audio <br>
        * TTML <br>
        * Application and Index Item <br>
        * General Purpose Data <br>
    * Signalling Message <br>
        * PA Message <br>
        * CA Message <br>
        * Data Transmission Message <br>
        * M2 Section Message <br>
        * M2 Short Section Message <br>

These messages are deliverying table like following :

Tables of based on B10 : 
* Bouquet Association Table (BAT)
* Broadcaster Information Table (BIT)
* Conditional Access Table (CAT)
* Discontinuity Information Table (DIT)
* Download Control Message (DCM)
* Entitlement Control Message (ECM)
* Entitlement Management Message (EMM)
* Event Information Table (EIT)
* Event Relation Table (ERT)
* Index Transmission Table (ITT)
* Linked Description Table (LDT)
* Local Event Information Table (LEIT)
* Network Board Information Table (NBIT)
* Network Information Table (NIT)
* Partial Content Announcement Table (PCAT)
* Program Association Table (PAT)
* Program Map Table (PMT)
* Running Status Table (RST)
* Selection Information Table (SIT)
* Service Description Table (SDT)
* Software Download Trigger Table (SDTT)
* Stuffing Table (ST)
* Time Date Table (TDT)
* Time Offset Table (TOT)

Tables of based on B60 
* Conditional Access Table (CAT)
* Data Directory Management Table (DDMT)
* Data Asset Management Table (DSMT)
* Data Content Configuration Table (DCCT)
* Download Control Message (DCM)
* Download Management Message (DMM)
* Entitlement Control Message (ECM)
* Entitle Management Message (EMM)
* Event Message Table (EMT)
* Layout Configuration Table (LCT)
* MH-Application Information Table (MH-AIT)
* MH-Broadcaster Information Table (MH-BIT)
* MH-Common Data Table (MH-CDT)
* MH-Event Information Table (MH-EIT)
* MH-Service Description Table (MH-SDT)
* MH-Sofrware Download Trigger Table (MH-SDTT)
* MH-Time Offset Table (MH-TOT)
* MMH-Package Table (MMT-PT)
* Package List Table (PLT)

Above all tables can include descriptors like following :

Descriptors :
* Access Control Descriptor 
* Application Service Descriptor
* Asset Group Descriptor
* Audio Specific Config
* Background Color Descriptor
* Content Copy Control Descriptor
* Content Usage Control Descriptor
* Dependency Descriptor
* Emergency Information Descriptor
* Emergency News Descriptor
* Event Package Descriptor
* IP Data Flow Descriptor
* Lined PU Descriptor
* Locked Cache Descriptor
* Message Authentication Method Descriptor
* MH-Application Boundary and Permission Descriptor
* MH-Application Descriptor
* MH-Application Expiration Descriptor
* MH-Audio Component Descriptor
* MH-Autostart Priority Descriptor
* MH-Broadcaster Name Descriptor
* MH-Cache Control Info Descriptor
* MH-CA Contract Info Descriptor
* MH-CA Service Descriptor
* MH-CA Startup Descriptor
* MH-Component Group Descriptor
* MH-Compression Type Descriptor
* MH-Content Descriptor
* MH-Data Component Descriptor
* MH-Download Protection Descriptor
* MH-Event Group Descriptor
* MH-Expire Descriptor
* MH-Extended Event Descriptor
* MH-External Application Control Descriptor
* MH-HEVC Descriptor
* MH-Hierachy Descriptor
* MH-Info Descriptor
* MH-Linkage Descriptor
* MH-Local Time Offset Descriptor
* MH-Logo Transmission Descriptor
* MH-MPEG4 Audio Descriptor
* MH-MPEG4 Audio Extension Descriptor
* MH-Network Download Content Descriptor
* MH-Parental Rating Descriptor
* MH-Playback Application Descriptor
* MH-Randomized Latency Descriptor
* MH-Series Descriptor
* MH-Service Descriptor
* MH-Service List Descriptor
* MH-Short Event Descriptor
* MH-Simple Application Location Descriptor
* MH-Simple Playback Application Location Descriptor
* MH-SI Parameter Descriptor
* MH-Stream Identifier Descriptor
* MH-Target Region Descriptor
* MH-Transport Protocol Descriptor
* MH-Type Descriptor
* MPU-Download Content Descriptor
* MPU-Extended Timestamp Descriptor
* MPU-Node Descriptor
* MPU-Presentation Region Descriptor
* MPU-Timestamp Descriptor
* Multimedia Service Information Descriptor
* PU Structure Descriptor
* Related Broadcaster Descriptor
* Scrambler Descriptor
* Unlocked Cache Descriptor
* UTC NPT Reference Descriptor
* Video Component Descriptor

# Architecture

Every Tables and Descriptors are based on base/Table or arib/Table and so on what can be base of all of tables, and the bases are including BitReadWriter which can read/write bit values.

## Case Of DVB
* SectionFactory : User can use this factory to get table if user doesn't know a kind of table.
* BitReadWriter : utility which can read/write bitstream from/into section buffer.
* Table : base class of all of table.
* Descriptor : base class of all of descriptor

<center>
    <img src="https://github.com/tehokang/documents/blob/master/sedec/basic-table-concept.png?raw=true"/>
</center>

## Case Of ARIB

<center>
    <img src="https://github.com/tehokang/documents/blob/master/sedec/sedec-2.0-arib.png?raw=true"/>
</center>

## Package Tree

<pre>
.
├── sedec2 : main package name
│   ├── arib : tables, descriptors for ARIB
│   ├── base : tables, descriptor of base class
│   ├── dvb : tables, descriptors for DVB
│   └── util : utility like logger
└── zexamples
    └── decoder : examples of both arib, dvb
</pre>

# How To Build

We could build with Apache Ant as build tool, please install ant in your system. Following is explaining with prerequisite you already installed. Obviously ant build tool has almost same mechanism with make tool like make has target, dependencies and command.

Basically targets of ant has followings :
 * build (default)
 * build-project : To build with versioning up
 * clean : To delete bin and dist directory
 * deploy : To deploy remote server 
 * doc : To generate javadoc
 * init : To initialize build environment
 * release-arib-example-decoder : To make arib example which can decode arib section as jar
 * release-dvb-example-decoder : To make dvb example which can decode dvb section as jar
 * release-sdk : To release sedec as library
 * release-sdk-obfuscate : To relese sedec as obfuscate like being minified

You can type commands to build one of targets
<pre>
$cd sedec-2.0-java
$ant release-arib-example-decoder
</pre>

To use this in android, you could just import this library in your gradle of project.

# How To Run

After building a target, release-arib-example-decoder via Eclipse(or ANT), you can run with a parameter as section path dumped.

* 1st method of run
<pre>
$cd bin
$java -classpath . zexamples.decoder.AribTableDecoder ../tables-dumped/arib/b39/ait/mh-ait.bin
</pre>

* 2nd method of run
<pre>
$cd dist
$java -jar sedec2-2.0-example-arib-decoder.jar ../tables-dumped/arib/b10/pat/pat_dump_0
</pre>

As a result, the example will show up like below,
```
########### Byte Align ###########
001 : 00 b0 25 40 f1 c1 00 00 00 00
002 : e0 10 00 65 e1 f0 00 66 e2 f0
003 : 02 bc f0 f0 02 bd f1 f0 02 c3
004 : f7 f0 03 a1 e7 01 01 aa 7b 24
005 :
###################################
SEDEC2[D] ======= Section Header ======= (sedec2.arib.b10.tables.ProgramAssociationTable)
SEDEC2[D] table_id : 0x0
SEDEC2[D] section_syntax_indicator : 0x1
SEDEC2[D] section_length : 0x25 (37)
SEDEC2[D] ------------------------------
SEDEC2[D] transport_stream_id : 0x40f1
SEDEC2[D] version_number : 0x0
SEDEC2[D] current_next_indicator : 0x1
SEDEC2[D] section_number : 0x0
SEDEC2[D] last_section_number : 0x0
SEDEC2[D] 	 program_number : 0x0
SEDEC2[D] 	 pid : 0x10
SEDEC2[D] 	 program_number : 0x65
SEDEC2[D] 	 pid : 0x1f0
SEDEC2[D] 	 program_number : 0x66
SEDEC2[D] 	 pid : 0x2f0
SEDEC2[D] 	 program_number : 0x2bc
SEDEC2[D] 	 pid : 0x10f0
SEDEC2[D] 	 program_number : 0x2bd
SEDEC2[D] 	 pid : 0x11f0
SEDEC2[D] 	 program_number : 0x2c3
SEDEC2[D] 	 pid : 0x17f0
SEDEC2[D] 	 program_number : 0x3a1
SEDEC2[D] 	 pid : 0x701
SEDEC2[D] checksum_CRC32 : 0x01aa7b24
SEDEC2[D] ======================================
```

If you have any questions and if the app is misbehaving or missing critical features write me at [tehokang@gmail.com](mailto:tehokang@gmail.com).

# To Do

* I'm not sure what encoding of CRC32 is working correctly, the shift model can be wrong.
* More tables what you could know
