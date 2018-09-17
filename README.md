
# Introduction
SEDEC is to easily make decoder(and even encoder) of tables of DVB, ARIB as MPEG sections and whatever inherited from ISO-13818. SEDEC stand for **SE**ction **DEC**oder. Here whatever mean whoever could put new sections into SEDEC with easy steps. 

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
Every Tables and Descriptors are based on base/Table or arib/Table and so on what can be base, and the bases are including utility named as BitReadWriter to do read/write bit values.

- Need to add figure

# Source Tree
-

# How To Build
- Not yet provided as library, you could make library via ANT easily in java system. <br>
- Actually this project could be built after loading sources on Eclipse then the class files can be run on your console, especially bin/zexamples including sample executables.


# How To Run
<pre>
$java -classpath . .....
</pre>


If you have any questions and if the app is misbehaving or missing critical features write me at [tehokang@gmail.com](mailto:tehokang@gmail.com).
