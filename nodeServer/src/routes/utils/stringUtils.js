/*
 * stringUtils
 * @date 2020-06-03
 * @author LL Chung10 
 */

//req.body -> object params 
exports.setParams = function(params){
    var returnP = {};
    for( var i=0 ; i<Object.keys(params).length ; i++){
        returnP[  Object.keys(params)[i]  ] = Object.values(params)[i] ;
    };
    return returnP;
};
