import os
import sys
from pcapfile import savefile

if len(sys.argv) < 2:
	print "Usage : 192PCAP2Raw.py <PCAP_FILE> [Output RAW_FILE]\n"
	quit()

testCap_fileName = sys.argv[1]
outputRaw_fileName = testCap_fileName + ".raw"
if sys.argv == 3:
	outputRaw_fileName = sys.argv[2]

testCap = open(testCap_fileName, 'rb')
capFile = savefile.load_savefile(testCap)
outputRaw = open(outputRaw_fileName, 'wb')

for pktC in range(0, len(capFile.packets)):
	if capFile.packets[pktC].packet_len < 28:
		print "Malform Packet Detected!"
		continue
	if capFile.packets[pktC].packet_len == 33:
		print "Empty PDU Discarded!"
		continue
	strPkt = capFile.packets[pktC].raw()
	for strPktC in (28, len(strPkt) - 1):
		if strPktC == 28:
			chkStr = strPkt[strPktC].encode("hex")
			if chkStr[1] == '0':
				print "Advertisement Message Discarded!";
				break
			if chkStr[1] == '1':
				print "Advertisement Direct Message Discarded!"
				break
			if chkStr[1] == '2':
				print "Advertisement Non-Connection Message Discarded!"
				break
			if chkStr[1] == '3':
				print "Scan Request Message Discarded!"
				break
			if chkStr[1] == '4':
				print "Scan Response Message Discarded!"
				break
		if strPktC > 29:
			outputRaw.write(strPkt[strPktC])

outputRaw.flush()
print "Done!\n"


	
	
