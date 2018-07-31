var sys = require('util');
var fs = require('fs');
var exec = require('child_process').exec;
var schedule = require('node-schedule');
var request = require('request');

console.log(req);

function puts(error, stdout, stderr) { //sys.puts(stdout) 
    console.log(stdout);
    console.log(stderr);
}

exec("ubertooth-btle -f -c output.pcap", puts);

schedule.scheduleJob('*/10 * * * * *',function(){
    exec("./crackle -i output.pcap", puts);
});


