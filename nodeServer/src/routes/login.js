/*
 * login
 * @date 2019-05-07
 * @author LCY
 */

var login = async function(req, res) {

  if( typeof( req.session.user ) != 'undefined' ){
    res.redirect('/main');
  }else{
    res.render('web/login.ejs', {param:req ,  message:"none"});
  };

};


var loginChk = async function(req, res) {

  var p_user_id = (req.body.user_id || req.query.user_id);
  var p_user_pwd = (req.body.user_pwd || req.query.user_pwd);
  
  var database = req.app.get('database');
  
  var mapperNm = 'mainMapper'
	var queryId='getLogin'
  var param = { 
      USER_PWD: p_user_pwd ,
      USER_ID:p_user_id 
  };
  
  var result = await database( mapperNm , queryId , param );
  
  if( result[0].CHECK_PWD == 1){
    //로그인 성공후 session 담기
    req.session.user = result[0];
    res.redirect('/main');
  }else{
    var msg = "아이디를 확인해주세요.!";
    res.render('web/login.ejs', {param:req ,  message:msg});
  }

};

var loginOut = async function(req, res) {
  req.session.destroy(function(){ 
    req.session;
    res.redirect('/');
  });
};



module.exports.login = login;
module.exports.loginChk = loginChk;
module.exports.loginOut = loginOut;




