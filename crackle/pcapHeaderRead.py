import os
import sys
from pcapfile import savefile

if len(sys.argv) < 2:
	print "Usage: pcapHeaderRead.py PCAP_FILE";
	quit();

testCap_fileName = sys.argv[1]
testCap = open(testCap_fileName, 'rb')
capFile = savefile.load_savefile(testCap);

print testCap
print "\n"
print "Capfile Summary:"
print capFile
print "Byte Order:"
print capFile.header.byteorder
print "Link Layer Type:"
print capFile.header.ll_type
print "Magic Number:"
print capFile.header.magic
print "Major Version Number:"
print capFile.header.major
print "Minor Version Number:"
print capFile.header.minor
print "Max Length of Each Packet:"
print capFile.header.snaplen
print "Nano Second Resolution:"
print capFile.header.ns_resolution
print "TimeZone TS_ACC:"
print capFile.header.ts_acc
print "TimeZone TZ_OFF:"
print capFile.header.tz_off
