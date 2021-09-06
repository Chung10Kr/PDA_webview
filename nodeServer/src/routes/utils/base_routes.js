/*
 * Main
 * @date 2019-05-07
 * @author LCY
 */


var stringUtils = require("./stringUtils");

/*
공통 라우터
필수 파라미터
url : "/comRouter";
mapperId : "매퍼 아이디"
queryId : "쿼리 아이디"
*/
var comRouter = async function(req, res) {
  var params =  stringUtils.setParams( req.body );
  var database = req.app.get('database');
  var result = await database( params.mapperId, params.queryId , params );
  res.json( result );
};


module.exports.comRouter = comRouter;



