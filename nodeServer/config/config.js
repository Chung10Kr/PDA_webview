/*
 * 설정
 */
var property = require("./property");

module.exports = {
	server_port:property.server_port,
	sqlConfig:property.sqlConfig,

	//파일 위치 (%소스 위치 아님)
	files_location : property.files_location,
	//파일 업로드 위치 
	upload_location : property.files_location + "/upload",
	//APK 업로드 위치
	apk_location : property.files_location + "/apk",
	basic_location : property.basic_location,
	//라우터 설정
	route_info:[
			
			//메인화면
			{file:'../src/routes/login', path:'/', method:'login', type:'get'},
			{file:'../src/routes/main', path:'/main', method:'main', type:'get'},

			//로그인 
			{file:'../src/routes/login', path:'/login/login', method:'login', type:'get'},
			{file:'../src/routes/login', path:'/login/loginChk', method:'loginChk', type:'post'},
			{file:'../src/routes/login', path:'/login/logOut', method:'loginOut', type:'get'},

			//APK 관련 라우트
			{file:'../src/routes/utils/fileManager', path:'/apkView', method:'apkView', type:'get'},
			{file:'../src/routes/utils/fileManager', path:'/uploadApk', method:'uploadApk', type:'post'},
			{file:'../src/routes/utils/fileManager', path:'/chkApk', method:'chkApk', type:'post'},
			{file:'../src/routes/utils/fileManager', path:'/apkQr', method:'apkQr', type:'get'},
			{file:'../src/routes/utils/fileManager', path:'/apkQrDown', method:'apkQrDown', type:'get'},
			
			//FCM 푸쉬알람
			{file:'../src/routes/utils/fcmManager', path:'/fcmView', method:'fcmView', type:'get'},
			{file:'../src/routes/utils/fcmManager', path:'/fcmMsg', method:'setToken', type:'post'},
			{file:'../src/routes/utils/fcmManager', path:'/sendFcm', method:'sendFcm', type:'post'},
			
			//이미지 업로드
			{file:'../src/routes/utils/fileManager', path:'/uploadImg', method:'uploadImg', type:'post'},

			//Code 
			{file:'../src/routes/main', path:'/codeUtil/codeListAjax', method:'getCode', type:'post'},
			


			/*모바일 API START*/
			{file:'../src/routes/mobile', path:'/m/login', method:'login', type:'post'},
			{file:'../src/routes/mobile', path:'/m/main', method:'main', type:'get'},
			{file:'../src/routes/mobile', path:'/m/menu', method:'menu', type:'post'},

			/* Web RTC */
			{file:'../src/routes/utils/rtc', path:'/m/rtc', method:'getRtc', type:'get'},

			/*Sample*/
			{file:'../src/routes/sample', path:'/m/user',       method:'userList', type:'get'},
			{file:'../src/routes/sample', path:'/m/user/table', method:'userListTable', type:'post'},
			{file:'../src/routes/sample', path:'/m/sample',     method:'sample', type:'get'},
			{file:'../src/routes/sample', path:'/m/sample/Ajax',method:'sampleCall', type:'post'},
			
	],

	// Mybatis Mapper 설정
	mapper_info:[
		{mapperNm : 'mainMapper' , path: './src/mapper/mainMapper.xml'},
		{mapperNm : 'apkMapper' , path: './src/mapper/apkMapper.xml'},
		{mapperNm : 'fcmMapper' , path: './src/mapper/fcmMapper.xml'},
		{mapperNm : 'commonMapper' , path: './src/mapper/commonMapper.xml'},

		{mapperNm : 'sampleMapper' , path: './src/mapper/sampleMapper.xml'},
		
		
	],

	//Auth Check Exception
	auth_Exception:[
		"/",
		"/login/login",
		"/login/loginChk",
		"/chkApk",
		"/fcmMsg",
		"/apkQrDown",
		"/m/login",
		"/codeUtil/codeListAjax"
	],
	
}

