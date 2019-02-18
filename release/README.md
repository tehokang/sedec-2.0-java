# SEDEC README

SEDEC is to easily make decoder(and even encoder) of tables of DVB, ARIB as MPEG sections and whatever inherited from ISO-13818. SEDEC stand for <font color="red">**SE**</font>ction <font color="red">**DEC**</font>oder.

## Download Source Code
You can download the sedec source code as below. <br>
**git clone ssh://${USER}@exg.humaxdigital.com:29418/Component/sedec2** <br>

## Verify the integrity
Every SEDEC release includes MD5 hash code to verify the integrity of library. <br>
If the library seems to be abnormal, you can easily verify the released sedec library as below.
> md5sum sedec-${ver}.jar <br>
> \> a9216d662f94ca1020a4669b6479b040  sedec-x.x.jar <br>

Compare the generated hash code with the hash code in Changelog.md. <br>
If the same as the hash code in Changelog.md, the library has integrity.

## Documentation
SEDEC documentation is available in the humax intranet [site](http://humax-browser.iptime.org/members/thkang2/sedec2/doc/external/index.html).

## License
SEDEC is licensed under the GNU Lesser General Public License (LGPL) version 3. 

## Examples
There are examples in *src/main/java/zexamples* of source tree. <br>