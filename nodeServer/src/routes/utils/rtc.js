/*
 * Web RTC(Real Time Communication)
 * @date 2021-10-11
 * @author 바다쓰기
 */

var getRtc = async function(req, res) {
  res.render('mobile/rtc/rtc', {param:req});
};


module.exports.getRtc = getRtc;




