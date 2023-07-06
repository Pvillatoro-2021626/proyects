Drop database if exists DBMuelitas2021626;
Create database DBMuelitas2021626;

use DBMuelitas2021626;

Create table Paciente(
	codigoPaciente int not null,
    nombresPaciente varchar(50) not null,
    apellidosPaciente varchar(50) not null,
    sexo char not null,
    fechaNacimiento date not null,
    direccionPaciente varchar(100) not null,
    telefonoPersonal varchar(8) not null,
    fechaPrimeraVisita date,
    primary key PK_codigoPersonal (codigoPaciente)
);

Create table Especialidades(
	codigoEspecialidad int not null auto_increment,
    descripcion varchar(100) not null,
    primary key PK_codigoEspecialidad (codigoEspecialidad)
);

Create table Medicamentos(
	codigoMedicamento int not null auto_increment,
    nombresMedicamento varchar(100) not null,
    primary key PK_codigoMedicamento (codigoMedicamento)
);

Create table Doctores(
	numeroColegiado int not null,
    nombresDoctor varchar(50) not null,
    apellidosDoctor varchar(50) not null,
    telefonoContacto int not null,
    codigoEspecialidad int not null,
    primary key PK_numeroColegiado (numeroColegiado),
    constraint FK_Doctores_Especialidades foreign key (codigoEspecialidad)
		references Especialidades (codigoEspecialidad)
);

Create table Recetas(
	codigoReceta int not null auto_increment,
    fechaReceta date not null,
    numeroColegiado int not null,
    primary key PK_codigoReceta (codigoReceta),
    constraint FK_Receta_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

Create table DetalleReceta(
	codigoDetalleReceta int not null auto_increment,
    dosis varchar(100) not null,
    codigoReceta int not null,
    codigoMedicamento int not null,
    primary key PK_codigoDetalleReceta (codigoDetalleReceta),
    constraint FK_DetalleReceta_Receta foreign key (codigoReceta)
		references Recetas (codigoReceta),
	constraint Fk_DetalleReceta_Medicamentos foreign key (codigoMedicamento)
		references Medicamentos(codigoMedicamento)
);

Create table Citas(
	codigoCita int not null auto_increment,
	fechaCita date not null,
    horaCita time not null,
    tratamiento varchar(150),
    descripCondActual varchar(255) not null,
    codigoPaciente int not null,
    numeroColegiado int not null,
    primary key PK_codigoCita (codigoCita),
    constraint FK_Citas_Paciente foreign key (codigoPaciente)
		references Paciente (codigoPaciente),
	constraint FK_Citas_Doctores foreign key (numeroColegiado)
		references Doctores (numeroColegiado)
);

-- ----------------------PACIENTES--------------------------
-- -----------------Procedimiento Agregar--------------------
Delimiter $$
	Create procedure sp_AgregarPaciente(in codigoPaciente int, in nombresPaciente varchar(50),in apellidosPaciente varchar(50), in sexo char,
		in fechaNacimiento date, in direccionPaciente varchar(100), in telefonoPersonal varchar(8), in fechaPrimeraVisita date)
		Begin
        Insert into Paciente (codigoPaciente, nombresPaciente, apellidosPaciente , sexo,
								fechaNacimiento, direccionPaciente , telefonoPersonal, fechaPrimeraVisita)
					value (codigoPaciente, nombresPaciente, apellidosPaciente ,upper(sexo),
								fechaNacimiento, direccionPaciente , telefonoPersonal, fechaPrimeraVisita);
		End$$
Delimiter ;
call sp_AgregarPaciente(1001,'Patrick','Villatoro','M','2005-07-20','Villa Hermosa zona 7','58444900','2022-04-19');
call sp_AgregarPaciente(1002,'Kevin','Gonzales','M','2005-07-20','Villa Hermosa zona 7','58444900','2022-04-19');

-- -----------------------Procedimiento Listar------------------
Delimiter $$
	Create procedure sp_ListarPaciente()
		Begin
			Select 
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo,
				P.fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
			from Paciente P ;
        End$$
Delimiter ;
call sp_ListarPaciente();
-- ----------------------Procedimiento Buscar---------------------
Delimiter $$
	Create procedure sp_BuscarPaciente(in codPaciente int)
		Begin
			Select
				P.codigoPaciente, 
				P.nombresPaciente, 
				P.apellidosPaciente, 
				P.sexo,
				P.fechaNacimiento, 
				P.direccionPaciente, 
				P.telefonoPersonal, 
				P.fechaPrimeraVisita
			From Paciente P where codPaciente=codigoPaciente;
		End$$
Delimiter ;
call sp_BuscarPaciente(1001);
-- ---------------------Procedimieto Eliminar----------------------
Delimiter $$
	Create procedure sp_EliminarPaciente(in codiPaciente int)
		Begin
			Delete from Paciente
				where codiPaciente=codigoPaciente;
		 End$$
Delimiter ;
-- ---Call sp_EliminarPaciente(1);
call sp_ListarPaciente();
-- --------------------Procedimiento Editar------------------------
Delimiter $$
	Create procedure sp_EditarPaciente(in codPaciente int, in nomPaciente varchar(50),in apePaciente varchar(50), in sex char,
		in fechaNaci date, in direPaciente varchar(100), in telPersonal varchar(8), in fechaPV date)
        Begin
			update Paciente P
				set
					P.nombresPaciente = nomPaciente, 
					P.apellidosPaciente = apePaciente,
					P.sexo = sex,
					P.fechaNacimiento = fechaNaci, 
					P.direccionPaciente = direPaciente,
					P.telefonoPersonal = telPersonal,
					P.fechaPrimeraVisita = fechaPV
                    where codPaciente=codigoPaciente;
		End$$
Delimiter ;
call sp_EditarPaciente(1002,'Eduardo','Ic','m','2005-07-20','Villa Hermosa zona 7','58444900','2022-04-19');
call sp_ListarPaciente();

-- ---------------------ESPECIALIDADES ----------------------------
-- --------------------Procedimiento Agregar------------------------
Delimiter $$
	Create procedure sp_AgregarEspecialidad(in descripcion varchar(100))
		Begin
			Insert into Especialidades (descripcion)
				value (descripcion);
		End$$
Delimiter ;
call sp_AgregarEspecialidad('Cirujanos');
call sp_AgregarEspecialidad('Ortodoncia');
-- -------------------Procedimiento Listar------------------------
Delimiter $$
	Create procedure sp_ListarEspecialidades()
		Begin
			Select 
				E.codigoEspecialidad,
				E.descripcion
					from Especialidades E;
		End$$
Delimiter ;
call sp_ListarEspecialidades();
-- --------------------Procedimiento Buscar---------------------
Delimiter $$
	Create procedure sp_BuscarEspecialidad(in codEspe int)
		Begin
			Select
				E.codigoEspecialidad,
                E.descripcion
					from Especialidades E where codEspe=codigoEspecialidad;
		End$$
Delimiter ;
call sp_BuscarEspecialidad(1);
-- --------------------Procedimiento Eliminar-------------------
Delimiter $$
	Create procedure sp_EliminarEspecialidad(in codEspe int)
		Begin 
			Delete from Especialidades
				where codEspe=codigoEspecialidad;
		End$$
Delimiter ;
-- -----call sp_EliminarEspecialidad(1);
call sp_ListarEspecialidades();
-- -------------------Procedimiento Editar-----------------------
Delimiter $$
	Create procedure sp_EditarEspecialidad(in codEspe int, in descri varchar(100))
		Begin
			Update Especialidades E
				set
					E.descripcion=descri
						where codEspe=codigoEspecialidad;
		End$$
Delimiter ;
call sp_EditarEspecialidad(2,'Medico');
call sp_ListarEspecialidades();
-- --------------------MEDICAMENTOS---------------------------------
-- --------------------Agregar Medicamentos-------------------------
Delimiter $$
	Create procedure sp_AgregarMedicamentos(in nombresMedicamento varchar(100))
		Begin
			Insert into Medicamentos (nombresMedicamento)
				value (nombresMedicamento);
		End$$
Delimiter ;
call sp_AgregarMedicamentos('Ampicilina');
call sp_AgregarMedicamentos('Tetraciclina');
-- --------------------Listar Medicamentos---------------------------
Delimiter $$
	Create procedure sp_ListarMedicamento()
		Begin 
			Select
				M.codigoMedicamento,
                M.nombresMedicamento
					from Medicamentos M;
		End $$
Delimiter ;
call sp_ListarMedicamento();
-- --------------------Buscar Medicamentos----------------------------
Delimiter $$
	Create procedure sp_BuscarMedicamentos(in codMedi int)
		Begin 
			Select 
				M.codigoMedicamento,
                M.nombresMedicamento
					from Medicamentos M where codMedi=codigoMedicamento;
		End $$
Delimiter ;
call sp_BuscarMedicamentos(1);
-- -------------------Eliminar Medicamentos---------------------------
Delimiter $$
	Create procedure sp_EliminarMedicamento(in codMed int)
		Begin
			Delete from Medicamentos 
				where codMed=codigoMedicamento;
		End$$
Delimiter ;
-- --call sp_EliminarMedicamento(1);
call sp_ListarMedicamento();
-- ------------------------Editar Medicamentos-----------------------------
Delimiter $$
	Create procedure sp_EditarMedicamentos(in codMed int,in nombre varchar(100))
		Begin 
			Update Medicamentos M
				set
					M.nombresMedicamento=nombre
						where codMed = codigoMedicamento;
		End$$
Delimiter ;
call sp_EditarMedicamentos(2,'ibuprofeno');
call sp_ListarMedicamento();
-- -------------------------------DOCTORES------------------------------------
-- ---------------------------Agregar Doctores--------------------------------
Delimiter $$
	Create procedure sp_AgregarDoctores(in numeroColegiado int, in nombresDoctor varchar(50), in apellidosDoctor varchar(50), 
							in telefonoContacto int, in codigoEspecialidad int)
			Begin
					Insert into Doctores (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad)
						value (numeroColegiado, nombresDoctor, apellidosDoctor, telefonoContacto, codigoEspecialidad);
			End $$
Delimiter ;
call sp_AgregarDoctores(5823,'Fredy','Diggory',87562413,2);
call sp_AgregarDoctores(8962,'Angel','Gutierrez',82647319,2);
-- --------------------------Listar Doctores------------------------------
Delimiter $$
	Create procedure sp_ListarDoctor()
		Begin
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
					from Doctores D;
		End$$
Delimiter ;
call sp_ListarDoctor();
-- ---------------------------Buscar Doctores-----------------------------
Delimiter $$
	Create procedure sp_BuscarDoctores(in codDoc int)
		Begin 
			Select
				D.numeroColegiado,
                D.nombresDoctor,
                D.apellidosDoctor,
                D.telefonoContacto,
                D.codigoEspecialidad
					from Doctores D where codDoc=numeroColegiado;
		End$$
Delimiter ;
call sp_BuscarDoctores(5823);
-- ---------------------------Eliminar Doctores---------------------------
Delimiter $$
	Create procedure sp_EliminarDoctores(in codDoc int)
		Begin
			Delete from Doctores 
				where codDoc=numeroColegiado;
		End $$
Delimiter ;
-- -call sp_eliminarDoctores(5823);
call sp_ListarDoctor();
-- ---------------------------Editar Doctores------------------------------
Delimiter $$
	Create procedure sp_EditarDoctores(in numCole int, in nombre varchar(50), in apellido varchar(50), in tel int)
		Begin 
			Update Doctores D
				set
					D.nombresDoctor = nombre,
                    D.apellidosDoctor = apellido,
                    D.telefonoContacto = tel
						where numCole=Numerocolegiado;
		End$$
Delimiter ;
call sp_EditarDoctores(8962,'Carlos','Mendez',89523164);
call sp_ListarDoctor();
/*-- ------------------------- RECETAS-----------------------------------
-- --------------------------Agregar Recetas---------------------------
Delimiter $$
	Create procedure sp_AgregarRecetas(in fechaReceta date, numeroColegiado int)
		Begin
			Insert into Recetas (fechaReceta,numeroColegiado)
				value (fechaReceta,numeroColegiado);
		End$$
Delimiter ;
call sp_AgregarRecetas('2022-08-23',8962);
call sp_AgregarRecetas('2021-02-20',8962);
-- ------------------------Listar Recetas-------------------------------
Delimiter $$
	Create procedure sp_ListarRecetas()
		Begin	
			Select 
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
					from Recetas R;
        End$$
Delimiter ;
call sp_ListarRecetas();
-- ----------------------Buscar Recetas------------------------------
Delimiter $$
	Create procedure sp_BuscarReceta(in cod int)
		Begin
			Select 
				R.codigoReceta,
                R.fechaReceta,
                R.numeroColegiado
					from Recetas R where cod=codigoReceta;
        End $$
Delimiter ;
call sp_BuscarReceta(1);
-- -----------------------Eliminar Recetas---------------------------
Delimiter $$
	Create procedure sp_EliminarReceta(in cod int)
		Begin 
			Delete from Recetas 
				where cod=codigoReceta;
        End$$
Delimiter ;
call sp_EliminarReceta(2);
call sp_ListarRecetas();
-- ------------------------Editar Recetas----------------------------
Delimiter $$
	Create procedure sp_EditarReceta(in cod int, in fecha date, in numCole int)
		Begin 
			Update Recetas R
				set 
					R.fechaReceta = fecha,
                    R.numeroColegiado = numCole
						where cod = codigoReceta;
		End$$
Delimiter ;
call sp_EditarReceta(1,'2021-08-23',8962);
call sp_ListarRecetas();
-- ----------------------DETALLERECETAS----------------------------
-- ----------------------Agregar DetalleReceta---------------------
Delimiter $$
	Create procedure sp_AgregarDetalle(in dosis varchar(100), in codigoReceta int, in codigoMedicamento int)
		Begin
			Insert into DetalleReceta (dosis,codigoReceta,codigoMedicamento)
				value (dosis,codigoReceta,codigoMedicamento);
		End$$
Delimiter ;
call sp_AgregarDetalle('una capsula cada 12 horas',1,2);
call sp_AgregarDetalle('una capsula cada 6 horas',1,2);
-- ---------------------Listar DetalleReceta-------------------------
Delimiter $$
	Create procedure sp_ListarDetalle()
		Begin
			Select
				DR.codigoDetalleReceta,
                DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
					from DetalleReceta DR;
        End$$
Delimiter ;
call sp_ListarDetalle();
-- ----------------------Buscar DetalleReceta-----------------------
Delimiter $$
	Create procedure sp_BuscarDetalle(in cod int)
		Begin
			Select 
				DR.codigoDetalleReceta,
                DR.dosis,
                DR.codigoReceta,
                DR.codigoMedicamento
					from DetalleReceta DR where cod=codigoDetalleReceta;
        End$$
Delimiter ;
call sp_BuscarDetalle(1);
-- ----------------------Eliminar DetalleReceta--------------------
Delimiter $$
	Create procedure sp_EliminarDetalle(in cod int)
		Begin
			Delete from DetalleReceta
				where cod=codigoDetalleReceta;
        End$$
Delimiter ;
call sp_EliminarDetalle(1);
call sp_ListarDetalle();
-- ----------------------Editar DetalleReceta----------------------
Delimiter $$
	Create procedure sp_EditarDetalle(in cod int, in dosi varchar(100), in codigoRece int, in codigoMedi int)
		Begin
			Update DetalleReceta DR
				set
					DR.dosis = dosi,
					DR.codigoReceta = codigoRece,
					DR.codigoMedicamento = codigoMedi
						where cod = codigoDetalleReceta;
        End $$
Delimiter ;
call sp_EditarDetalle(2,'una capsula cada 12 horas',1,2);
call sp_ListarDetalle();
-- ---------------------------CITAS-------------------------------
-- ---------------------------Agregar Citas-----------------------
Delimiter $$
	Create procedure sp_AgregarCita(in fechaCita date, in horaCita time, in tratamiento varchar(150), in descripCondActual varchar(255), 
					in codigoPaciente int, in numeroColegiado int)
		Begin
			Insert into Citas (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado)
				value (fechaCita, horaCita, tratamiento, descripCondActual, codigoPaciente, numeroColegiado);
		End$$
Delimiter ;
call sp_AgregarCita('2022-01-23','12:24',' ','dolores y fiebre',1002,8962);
-- ---------------------------Listar Citas------------------------
Delimiter $$
	Create procedure sp_ListarCita()
		Begin
			Select 
				C.codigoCita,
				C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.descripCondActual, 
                C.codigoPaciente, 
                C.numeroColegiado
					from Citas C;
        End$$
Delimiter ;
call sp_ListarCita();
-- --------------------------Buscar Citas--------------------------
Delimiter $$
	Create procedure sp_BuscarCita(in cod int)
		Begin
			Select 
				C.codigoCita,
				C.fechaCita, 
                C.horaCita, 
                C.tratamiento, 
                C.descripCondActual, 
                C.codigoPaciente, 
                C.numeroColegiado
					from Citas C where cod=codigoCita;
        End$$
Delimiter ;
call sp_BuscarCita(1);
-- -----------------------Eliminar Cita----------------------------
Delimiter $$
	Create procedure sp_EliminarCita(in cod int)
		Begin
			Delete from Citas
				where cod=codigoCita;
		End$$
Delimiter ;
call sp_EliminarCita(1);
call sp_ListarCita();
-- -----------------------Edita Cita------------------------------
Delimiter $$
	Create procedure sp_EditarCita(in cod int, in fecha date, in hora time, in trata varchar(150), in descripcion varchar(255), 
					in codPaci int, in numCole int)
			Begin
				Update Citas C
					set
						C.fechaCita = fecha, 
						C.horaCita = hora, 
						C.tratamiento = trata, 
						C.descripCondActual = descripcion, 
						C.codigoPaciente = codPaci, 
						C.numeroColegiado = numCole
							where cod = codigoCita;
						
            End$$
Delimiter ;
call sp_EditarCita(2,'2022-01-25','1:30',' ','fiebre',1002,8962);
call sp_ListarCita(); */