
# Table of Contents
* [Introduction](#Introduction)
	* [DVB](#DVB)
	* [ARIB](#ARIB)
* [Architecture](#Architecture)
	* [Package Tree](#Package-Tree)
* [How To Build](#How-To-Build)
* [How To Run](#How-To-Run)
* [TODO](#TODO)


# Introduction

SEDEC is to easily make decoder(and even encoder) of tables of DVB, ARIB as MPEG sections and whatever inherited from ISO-13818. SEDEC stand for <font color="red">**SE**</font>ction <font color="red">**DEC**</font>oder. Here whatever mean whoever could put new sections into SEDEC with easy steps. 

You could get started to make a kind of sections to decode like following easy step, <br>
* Create table based on base/Table in case of DVB, arib/Table in case of ARIB <br>
* Add parse routine in their constructor, basically the constructor get binary data starting table_id as known as beginning of the section.<br>
* Add getter function if you want to provide interfaces. <br>
* Add print function if you want to show the value of fields to be decoded. <br>

Currently SEDEC-2.0-java is including sections like below,

## DVB 

Tables as known as section : PAT, PMT, CAT, AIT, BAT, DIT, EIT, NIT, RST, SIT, SDT, ST, TDT, TOT

## ARIB 

Tables of based on B10 : BAT, BIT, CAT, DIT, DCM, ECM, EMM, EIT, ERT, ITT, LDT, LET, NBT, NIT, PCAT, PAT, PMT, RST, SIT, SDT, SDTT, ST, TDT, TOT

Tables of based on B39/60 : AMT, CAT, DSMT, DCCT, DDMT, DCM, DMM, ECM, EMM, EMT, LCT, MH-AIT, MH-BIT, MH-CDT, MH-EIT, MH-SDT, MH-SDTT, MH-TOT, MMT-PT, PLT, TLV-NIT

Messages of based on B60 : PAM, M2SM, M2SSM, CAM ,DTM

# Architecture

Every Tables and Descriptors are based on base/Table or arib/Table and so on what can be base of all of tables, and the bases are including BitReadWriter which can read/write bit values.

* SectionFactory : User can use this factory to get table if user doesn't know a kind of table.
* BitReadWriter : utility which can read/write bitstream from/into section buffer.
* Table : base class of all of table.
* Descriptor : base class of all of descriptor

<img src="https://github.com/tehokang/documents/blob/master/sedec/sedec-2.0-overview-general.png?raw=true"/>

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

* 1st case of run
<pre>
$cd bin
$java -classpath . zexamples.decoder.AribTableDecoder ../tables-dumped/arib/b39/ait/mh-ait.bin
</pre>

* 2nd case of run
<pre>
$cd dist
$java -jar sedec2-2.0-example-arib-decoder.jar ../tables-dumped/arib/b39/ait/mh-ait.bin
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
* Many kinds of table not including here
