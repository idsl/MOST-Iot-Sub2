var sys = require('util');
var exec = require('child_process').exec;
var schedule = require('node-schedule');
// var child;
// // executes `pwd`
// child = exec("pwd", function (error, stdout, stderr) {
//   sys.print('stdout: ' + stdout);
//   sys.print('stderr: ' + stderr);
//   if (error !== null) {
//     console.log('exec error: ' + error);
//   }
// });
// or more concisely
// var sys = require('sys')
// var exec = require('child_process').exec;
function puts(error, stdout, stderr) { //sys.puts(stdout) 
    console.log(stdout);
    console.log(stderr);
}

exec("./Blesniffer -c 37 demo.pcap", puts);

schedule.scheduleJob('*/10 * * * * *',function(){
    exec("./crackle -i output.pcap", puts);
});



// exec("./crackle -i output.pcap", puts);