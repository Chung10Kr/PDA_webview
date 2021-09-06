/*
 * fileManager
 * @date 2020-05-26
 * @author Chung10 
 */
var multiparty = require('multiparty');
var fs = require('fs');
var path = require('path');
var moment = require('moment');
const config = require('../../../config/config');


// APK 업로드 화면 이동
var apkView = async function(req,res){

      var database = req.app.get('database');
      var result = await database( "apkMapper", "checkApk_V" , {} );
      var version = result[0].APK_V
      res.render('web/apkView.ejs', {param:req , version:version});
};
//Qr코드로  Apk 다운
var apkQr = async function(req,res){
      var os = require('os');
      var ifaces = os.networkInterfaces();

      var local_ip;
      Object.keys(ifaces).forEach(function (ifname) {
      var alias = 0;
      ifaces[ifname].forEach(function (iface) {
            if ('IPv4' !== iface.family || iface.internal !== false) {
                  return;
            }
            ++alias;
            local_ip = iface.address;
            });
      });
      var apk_down = "http://"+local_ip+":"+config.server_port+"/apkQrDown";
      res.render('web/apkQr.ejs', {param:req , apk_url : apk_down });
};
//Qr코드로  Apk 다운
var mime = require('mime');
var apkQrDown = async function(req,res){
      
      var database = req.app.get('database');
      var result = await database( "apkMapper", "checkApk_V" , {} );
      var new_V = result[0].APK_V
      
      var file = config.apk_location+'/'+new_V+'/app-debug.apk' // ex) /upload/files/sample.txt
      try {
        if (fs.existsSync(file)) { // 파일이 존재하는지 체크
          var filename = path.basename(file); // 파일 경로에서 파일명(확장자포함)만 추출
          var mimetype = mime.getType(file); // 파일의 타입(형식)을 가져옴
          res.setHeader('Content-disposition', 'attachment; filename=' + filename); // 다운받아질 파일명 설정
          res.setHeader('Content-type', mimetype); // 파일 형식 지정
          var filestream = fs.createReadStream(file);
          filestream.pipe(res);
        } else {
          res.send('해당 파일이 없습니다.');  
          return;
        }
      } catch (e) { // 에러 발생시
        console.log(e);
        res.send('파일을 다운로드하는 중에 에러가 발생하였습니다.');
        return;
      }
};


// 이미지 업로드
var uploadImg = function(req, res){
      var returnPath='';
      var form = new multiparty.Form();

      // get field name & value
      form.on('field',function(name,value){
           
      });
      // file upload handling
      form.on('part',function(part){
            
           var filename;
           var size;
           if (part.filename) {
                 filename = part.filename;
                 size = part.byteCount;
           }else{
                 part.resume();
           }    
           //폴더 생성
          var file_location = req.app.set("config").upload_location;
          var dir = file_location ; 
          var paths = [moment().format("YYYY")  , moment().format("MM") , moment().format("DD")  ];
          for(var i=0; i < 3 ; i++){
            dir = path.join(dir , paths[0] );
            returnPath = path.join(returnPath , paths[0] );
            paths.splice(0,1);
            !fs.existsSync(dir)&&fs.mkdirSync(dir);
            
          };
          returnPath = path.join(returnPath , filename );
          
           var fullpath = path.join(dir , filename);

           var writeStream = fs.createWriteStream(fullpath);
           writeStream.filename = filename;
           part.pipe(writeStream);
           part.on('data',function(chunk){
           });
           part.on('end',function(){
                 writeStream.end();
           });
      });
      // all uploads are completed
      form.on('close',function(){
           res.status(200).send({filepath : returnPath});
      });
      // track progress
      form.on('progress',function(byteRead,byteExpected){
      });
      form.parse(req);

};


// APK 버전 체크
var chkApk = async function(req,res){

      var database = req.app.get('database');
      var result = await database( "apkMapper", "checkApk_V" , {} );
      var new_V = result[0].APK_V
    
      var APK_V = (req.body.APK_V || req.query.APK_V);

      var update_chk = "not";
      if( APK_V != new_V){
        // 업데이트 필요
        update_chk = "need"
      };
      var result = [{result:update_chk , new_version: new_V }];
      res.json( result );
    };

/**
 * 
 * @param {*} req 
 * @param {*} res 
 */
var uploadApk = async function (req, res) {

      var database = req.app.get('database');
      var result = await database( "apkMapper", "checkApk_V" , {} );
      console.log( result )
      var version = result[0].APK_V

      var versions =String( version+1 );

      //폴더 생성
      var file_location = req.app.set("config").apk_location;
      var dir = file_location ; 

      dir = path.join(dir , versions );

      // APK 정보 INSERT
      var params = {
            APK_V : versions,
            FILE_PATH : dir
      }
      var result = await database( "apkMapper", "insertApk" , params );

      var form = new multiparty.Form();
      // get field name & value
      form.on('field',function(name,value){
           
      });
      // file upload handling
      form.on('part',function(part){
           var filename;
           var size;
           if (part.filename) {
                 filename = part.filename;
                 size = part.byteCount;
           }else{
                 part.resume();
           }    
           //폴더 생성
          
          
           !fs.existsSync(dir)&&fs.mkdirSync(dir);

           var fullpath = path.join(dir , filename);

           var writeStream = fs.createWriteStream(fullpath);
           writeStream.filename = filename;
           part.pipe(writeStream);
           part.on('data',function(chunk){
           });
           part.on('end',function(){
                 writeStream.end();
           });
      });
      // all uploads are completed
      form.on('close',function(){
            res.json( 'SUC' );
      //      res.render('main.ejs', {param:req});
      });
      // track progress
      form.on('progress',function(byteRead,byteExpected){
           
      });
      form.parse(req);
};

    

module.exports.apkView = apkView;
module.exports.uploadImg = uploadImg;
module.exports.chkApk = chkApk;
module.exports.uploadApk = uploadApk;
module.exports.apkView = apkView;
module.exports.apkQr = apkQr;
module.exports.apkQrDown = apkQrDown;






