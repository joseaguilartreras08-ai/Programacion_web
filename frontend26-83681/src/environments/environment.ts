import packageInfo from '../../package.json';

export const environment = {
  appVersion: packageInfo.version,
  production: false,  
  // En el PC del laboratorio el puerto 8080 lo ocupa Oracle, por eso el backend corre en 8081.
  // En un equipo donde el 8080 esté libre se puede volver a 'http://localhost:8080/clinica/v1'.
  apiUrlAuth: 'http://localhost:8081/clinica/v1'
};