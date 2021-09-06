/*
 * Fcm Manager
 * @date 2020-05-26
 * @author LL Chung10 
 */



 /*
 * set View
 * @date 2020-05-26
 * @author Chung10 
 */
var fcmView = async function(req,res){
  var database = req.app.get('database');
  var result = await database( "fcmMapper", "selectFcm" , {} );
  res.render('web/fcmView.ejs', {param:req , resultList : result });
}



/*
 * save Token
 * @date 2020-05-26
 * @author Chung10 
 */
var setToken = async function(req,res){
  var device_token = (req.body.TOKEN || req.query.TOKEN);
  var user_id = (req.body.USER_ID || req.query.USER_ID);

  
  var database = req.app.get('database');
  
  

  var params = {
    TOKEN : device_token , 
    USER_ID : user_id
  };
  await database( "fcmMapper", "deleteFcm" , params );
  var result = await database( "fcmMapper", "insertFcm" , params );

  var result = [{result:"success"}];
  res.json( result );

};

var sendFcm = function(req,res){

  var device_token = (req.body.TOKEN || req.query.TOKEN);
  var MSG = (req.body.MSG || req.query.MSG);
  var TITLE = (req.body.TITLE || req.query.TITLE);
  sendPush(req , device_token , TITLE , MSG)


  var result = [{result:"success"}];
  res.json( result );

};

/*
 * send Push alarm
 * @date 2020-05-26
 * @author Chung10 
 */
// ex) sendPush(req , device_token , "푸쉬알람 사용" , '노드 서버의 fcmManager 참고 ')
var sendPush = function(req, device_token , title , msg){

  var serverKey = require('../../../fcm/android-8a2bb-firebase-adminsdk-40zl2-63bbb93d19.json') //put the generated private key path here    
  var admin = require('firebase-admin');
  
  if (!admin.apps.length) {
    const firebaseAdmin =  admin.initializeApp({
      credential: admin.credential.cert(serverKey),
    });
  }
  
  var fcm_msg = {
    notification:{
      title:title,
      body:msg
    }
  };

  admin.messaging().sendToDevice(device_token , fcm_msg)
  .then(function(response){ 
    
  }).catch(function(error){ 
    console.log("Push Error" + error); 
  });


};



module.exports.fcmView = fcmView;
module.exports.setToken = setToken;
module.exports.sendFcm = sendFcm;
module.exports.sendPush = sendPush;




