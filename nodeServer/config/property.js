/*
 * property
 */

module.exports = {

	files_location:'D:/Project/project_log/files' ,
	server_port:  '2000' ,
	basic_location:'D:/Project/project/40.seah/SOURCE/PDA/PDA/nodeServer',

	sqlConfig : {
		user: 'xxx',
		password: '#xxx',
		database: 'xxx',
		server: 'xx.xxx.xx.xx',
		pool: {
		  max: 10,
		  min: 0,
		  idleTimeoutMillis: 30000
		},
		options: {
		  encrypt: true, // for azure
		  trustServerCertificate: true // change to true for local dev / self-signed certs
		}
	  }
 
};