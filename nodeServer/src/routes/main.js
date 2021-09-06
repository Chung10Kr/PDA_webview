/*
 * Main
 * @date 2019-05-07
 * @author LCY
 */


var stringUtils = require("./utils/stringUtils");

//Main
var main = async function(req, res) {
  res.render('web/main.ejs', {param:req});
};

// 코드 가져오기
var getCode = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  var database = req.app.get('database');
  var codeList = await database( 'mainMapper' , 'getCode' , params );
  res.json({list:codeList});
};


module.exports.main = main;
module.exports.getCode = getCode;


