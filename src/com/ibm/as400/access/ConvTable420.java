//////////////////////////////////////////////////////////////////////
//
// IBM Confidential
//
// OCO Source Materials
//
// The Source code for this program is not published or otherwise
// divested of its trade secrets, irrespective of what has been
// deposited with the U.S. Copyright Office
//
// 5722-JC1
// (C) Copyright IBM Corp. 1997, 2000
//
////////////////////////////////////////////////////////////////////////
//
// File Name:    ConvTable420.java
//
// Description:  Contains Unicode converter tables for ccsid 420.
//
// Classes:      ConvTable420
//
////////////////////////////////////////////////////////////////////////
//
// CHANGE ACTIVITY:
//
// $A0=PTR/DCR   Release      Date        Userid    Comments
//     D98012    v5r1m0.jacl  10/29/1999  csmith    Created.
//               v5r1m0.jacl  05/11/2000  csmith    Regenerated.
//
//
// END CHANGE ACTIVITY
//
////////////////////////////////////////////////////////////////////////

package com.ibm.as400.access;

class ConvTable420 extends ConvTableBidiMap
{
  private static final String toUnicode_ = 
    "\u0000\u0001\u0002\u0003\u009C\t\u0086\u007F\u0097\u008D\u008E\u000B\f\r\u000E\u000F" +
    "\u0010\u0011\u0012\u0013\u009D\u0085\b\u0087\u0018\u0019\u0092\u008F\u001C\u001D\u001E\u001F" +
    "\u0080\u0081\u0082\u0083\u0084\n\u0017\u001B\u0088\u0089\u008A\u008B\u008C\u0005\u0006\u0007" +
    "\u0090\u0091\u0016\u0093\u0094\u0095\u0096\u0004\u0098\u0099\u009A\u009B\u0014\u0015\u009E\u001A" +
    "\u0020\u00A0\u0651\uFE7D\u0640\u200B\u0621\u0622\uFE82\u0623\u00A2\u002E\u003C\u0028\u002B\u007C" +
    "\u0026\uFE84\u0624\u001A\u001A\u0626\u0627\uFE8E\u0628\uFE91\u0021\u0024\u002A\u0029\u003B\u00AC" +
    "\u002D\u002F\u0629\u062A\uFE97\u062B\uFE9B\u062C\uFE9F\u062D\u00A6\u002C\u0025\u005F\u003E\u003F" +
    "\uFEA3\u062E\uFEA7\u062F\u0630\u0631\u0632\u0633\uFEB3\u060C\u003A\u0023\u0040\'\u003D\"" +
    "\u0634\u0061\u0062\u0063\u0064\u0065\u0066\u0067\u0068\u0069\uFEB7\u0635\uFEBB\u0636\uFEBF\u0637" +
    "\u0638\u006A\u006B\u006C\u006D\u006E\u006F\u0070\u0071\u0072\u0639\uFECA\uFECB\uFECC\u063A\uFECE" +
    "\uFECF\u00F7\u0073\u0074\u0075\u0076\u0077\u0078\u0079\u007A\uFED0\u0641\uFED3\u0642\uFED7\u0643" +
    "\uFEDB\u0644\uFEF5\uFEF6\uFEF7\uFEF8\u001A\u001A\uFEFB\uFEFC\uFEDF\u0645\uFEE3\u0646\uFEE7\u0647" +
    "\u061B\u0041\u0042\u0043\u0044\u0045\u0046\u0047\u0048\u0049\u00AD\uFEEB\u001A\uFEEC\u001A\u0648" +
    "\u061F\u004A\u004B\u004C\u004D\u004E\u004F\u0050\u0051\u0052\u0649\uFEF0\u064A\uFEF2\uFEF3\u0660" +
    "\u00D7\u2007\u0053\u0054\u0055\u0056\u0057\u0058\u0059\u005A\u0661\u0662\u001A\u0663\u0664\u0665" +
    "\u0030\u0031\u0032\u0033\u0034\u0035\u0036\u0037\u0038\u0039\u20AC\u0666\u0667\u0668\u0669\u009F";


  private static final String fromUnicode_ = 
    "\u0001\u0203\u372D\u2E2F\u1605\u250B\u0C0D\u0E0F\u1011\u1213\u3C3D\u3226\u1819\u3F27\u1C1D\u1E1F" +
    "\u405A\u7F7B\u5B6C\u507D\u4D5D\u5C4E\u6B60\u4B61\uF0F1\uF2F3\uF4F5\uF6F7\uF8F9\u7A5E\u4C7E\u6E6F" +
    "\u7CC1\uC2C3\uC4C5\uC6C7\uC8C9\uD1D2\uD3D4\uD5D6\uD7D8\uD9E2\uE3E4\uE5E6\uE7E8\uE93F\u3F3F\u3F6D" +
    "\u3F81\u8283\u8485\u8687\u8889\u9192\u9394\u9596\u9798\u99A2\uA3A4\uA5A6\uA7A8\uA93F\u4F3F\u3F07" +
    "\u2021\u2223\u2415\u0617\u2829\u2A2B\u2C09\u0A1B\u3031\u1A33\u3435\u3608\u3839\u3A3B\u0414\u3EFF" +
    "\u413F\u4A3F\u3F3F\u6A3F\u3F3F\u3F3F\u5FCA\uFFFF\u0014\u3F3F\u3FE0\uFFFF\u000F\u3F3F\u3FA1\uFFFF" +
    "\u0395\u3F3F\u3F5C\u6B4B\u6CDF\uEAEB\uEDEE\uEFFB\uFCFD\uFEB8\uB8B4\uB23F\uFFFF\u0004\u3F3F\u3F42" +
    "\u3F3F\u3F3F\u3F3F\uDCDA\uCFCD\uBEBC\uBAB0\uAEAC\u443F\u3F3F\u3F3F\u9F9D\u908F\u8E8C\u8A78\u7675" +
    "\u7473\u7270\u6866\u6462\u5957\u5556\u5249\u4746\u3FD0\u3F3F\u3FC0\uFFFF\u0007\u3F3F\u793F\uFFFF" +
    "\u0BC0\u3F3F\u3FE1\u3F3F\u3F45\uFFFF\u0050\u3F3F\uFA3F\uFFFF\u6D38\u3F3F\uF0F1\uF2F3\uF4F5\uF6F7" +
    "\uF8F9\u614B\u606B\u4E5C\u5D4D\u7D50\u6C5B\u7B7F\u5A3F\u3F3F\u3FB9\uB8B9\uB8B5\uB4B3\uB2DE\uDEDD" +
    "\uDCDB\uDACF\uCFCD\uCBBF\uBFBE\uBEBD\uBDBC\uBCBB\uBBBA\uBAB1\uB1B0\uB0AF\uAFAE\uAEAD\uADAC\uACAB" +
    "\uABAA\uA09F\u9E9D\u9C9B\u9A90\u9090\u908F\u8F8F\u8F8E\u8E8D\u8D8C\u8C8B\u8B8A\u8A80\u8078\u7877" +
    "\u7776\u7675\u7574\u7473\u7372\u7271\u7170\u7069\u6968\u6867\u6766\u6665\u6564\u6463\u6362\u6259" +
    "\u5958\u5857\u5655\u553F\u3F57\u5652\u5251\u4948\u4746\u3F3F\u4342\uFFFF\u01AF\u3F3F\u7A5E\u4C7E" +
    "\u6E6F\u7CC1\uC2C3\uC4C5\uC6C7\uC8C9\uD1D2\uD3D4\uD5D6\uD7D8\uD9E2\uE3E4\uE5E6\uE7E8\uE93F\u3F3F" +
    "\u3F6D\u3F81\u8283\u8485\u8687\u8889\u9192\u9394\u9596\u9798\u99A2\uA3A4\uA5A6\uA7A8\uA93F\u4F3F" +
    "\uFFFF\u0051\u3F3F";


  ConvTable420()
  {
    super(420, toUnicode_.toCharArray(), fromUnicode_.toCharArray());
  }
}
