/*
 * WebView Sample
 * @date 2021-09-04
 * @author LCY
 */


var stringUtils = require("./utils/stringUtils");

//사용자조회 화면 이동
var userList = async function(req, res) {
  res.render('mobile/sample/userList.ejs', {param:req});
};

//사용자조회 
var userListTable = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  var database = req.app.get('database');
  var resultList = await database( 'sampleMapper' , 'getUser' , params );
  res.render('mobile/sample/userListTable.ejs', {param:req, resultList:resultList});
};

var sample = async function(req, res) {
  res.render('mobile/sample/sample.ejs', {param:req});
};
var sampleCall = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  var msg = `${params.USER_NAME}님 안녕하세요 ^^`;
  res.json(msg);
};


module.exports.userList = userList;
module.exports.userListTable = userListTable;
module.exports.sample = sample;
module.exports.sampleCall = sampleCall;





