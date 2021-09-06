/*
 * Webview Main
 * @date 2021-02-23
 * @author LCY
 */


var stringUtils = require("./utils/stringUtils");

//로그인 액션
var login = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  var database = req.app.get('database');
  var result = await database( 'mainMapper' , 'getLogin' , params );


  res.json( result );
};

/*메인 페이지*/
var main = async function(req, res) {
  req.session.user = req.session.user || {
    USER_ID:  req.query.USER_ID || req.body.USER_ID,
    USER_NAME:  req.query.USER_NAME || req.body.USER_NAME
  };

  res.render('mobile/main.ejs', {param:req});
};

/*메뉴 가져오기*/
var menu = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  params["gUserId"] = res.locals.gUserId;
  var database = req.app.get('database');
  var resultList = await database( 'mainMapper' , 'getMenu' , params );
  res.render('mobile/menu.ejs', {param:req, menuList:resultList });
};

module.exports.login = login;
module.exports.main = main;
module.exports.menu = menu;



