var fs = require("fs");
var request = require("request");

var options = { method: 'POST',
  url: 'http://134.208.97.230:8080/nwSP80022/sp80022m-std.do',
  headers: 
   { 'postman-token': '03f7c6e8-2c66-7db3-4c02-d31d72198e44',
     'content-type': 'multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW',
     'cache-control': 'no-cache' },
  formData: 
   { bitCounts: '100000',
     Iterations: '1',
     rndFile: 
      { value: 'fs.createReadStream("192.pcap")',
        options: { filename: 'data.pi', contentType:'application/octet-stream' } },
     isAscii: 'binary',
     transRequest: 'tranba',
     upload: 'Upload' } };

request(options, function (error, response, body) {
  if (error) throw new Error(error);

  console.log(body);
});

