PGDMP     &    2                {            iFixCom    15.2    15.2 >    G           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            H           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            I           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            J           1262    18097    iFixCom    DATABASE     }   CREATE DATABASE "iFixCom" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';
    DROP DATABASE "iFixCom";
                postgres    false            �            1255    18098    hola(integer)    FUNCTION     #  CREATE FUNCTION public.hola(x integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
begin
	if(SELECT count(*) as contador FROM dispositivo WHERE id_cliente = x)>3 then
		Raise Notice 'Es mayor que 3';
		RETURN x + 1;
	else
		Raise Notice 'Es menor que 3';
		RETURN x + 2;
	end if;
end
$$;
 &   DROP FUNCTION public.hola(x integer);
       public          postgres    false            �            1255    18099    hola2(integer)    FUNCTION     '  CREATE FUNCTION public.hola2(x integer) RETURNS integer
    LANGUAGE plpgsql
    AS $$
begin
	if(SELECT count(*) as contador FROM dispositivo WHERE id_cliente = x)>3 then
		Raise Notice 'Es mayor que 3';
		RETURN x + 10;
	else
		Raise Notice 'Es menor que 3';
		RETURN x + 500;
	end if;
end
$$;
 '   DROP FUNCTION public.hola2(x integer);
       public          postgres    false            �            1255    18100    tr1()    FUNCTION     �   CREATE FUNCTION public.tr1() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
	if(SELECT count(*) as contador FROM dispositivo WHERE id_cliente = 1)>3 then
		Raise Notice 'Es mayor que 3';
	else
		Raise Notice 'Es menor que 3';
	end if;
end
$$;
    DROP FUNCTION public.tr1();
       public          postgres    false            �            1255    18101    tr2()    FUNCTION     m   CREATE FUNCTION public.tr2() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
	SELECT hola2(1);
end;
$$;
    DROP FUNCTION public.tr2();
       public          postgres    false            �            1255    18102    trg1()    FUNCTION     B  CREATE FUNCTION public.trg1() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
INSERT INTO empleado2 VALUES
(old.id_empleado, old.id_departamento,
old.nombre, old.direccion, old.colonia,
old.ciudad, old.cp, new.rfc, old.correo,
old.sueldo, old.nss, old.imagen, old.username,
old.contra, old.tipo);
RETURN NEW;
END
$$;
    DROP FUNCTION public.trg1();
       public          postgres    false            �            1255    18103    trg2()    FUNCTION     �   CREATE FUNCTION public.trg2() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

INSERT INTO orden2 VALUES 
(new.id_orden, new.id_dispo,
new.id_cliente, new.total, new.descuentos);

RETURN NEW;

END 
$$;
    DROP FUNCTION public.trg2();
       public          postgres    false            �            1259    18104    cliente    TABLE     �  CREATE TABLE public.cliente (
    id_cliente integer NOT NULL,
    nombre character varying(50) NOT NULL,
    direccion character varying(50) NOT NULL,
    colonia character varying(50) NOT NULL,
    ciudad character varying(50) NOT NULL,
    cp character varying(25) NOT NULL,
    correo character varying(30),
    telefono character varying(10),
    telefono2 character varying(10)
);
    DROP TABLE public.cliente;
       public         heap    postgres    false            �            1259    18107    cliente_id_cliente_seq    SEQUENCE     �   CREATE SEQUENCE public.cliente_id_cliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.cliente_id_cliente_seq;
       public          postgres    false    214            K           0    0    cliente_id_cliente_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.cliente_id_cliente_seq OWNED BY public.cliente.id_cliente;
          public          postgres    false    215            �            1259    18108    departamento    TABLE     �   CREATE TABLE public.departamento (
    id_departamento integer NOT NULL,
    nombre_depa character varying(50) NOT NULL,
    max_personas integer NOT NULL,
    ubicacion character varying(50) NOT NULL
);
     DROP TABLE public.departamento;
       public         heap    postgres    false            �            1259    18111     departamento_id_departamento_seq    SEQUENCE     �   CREATE SEQUENCE public.departamento_id_departamento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.departamento_id_departamento_seq;
       public          postgres    false    216            L           0    0     departamento_id_departamento_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.departamento_id_departamento_seq OWNED BY public.departamento.id_departamento;
          public          postgres    false    217            �            1259    18112    dispositivo    TABLE     �  CREATE TABLE public.dispositivo (
    id_dispo integer NOT NULL,
    sn character varying(50),
    tipo_dis character varying(30) NOT NULL,
    id_cliente integer NOT NULL,
    modelo character varying(40) DEFAULT NULL::character varying,
    estado_fisi character varying(100) NOT NULL,
    esta_recep character varying(100) NOT NULL,
    color character varying(30) NOT NULL,
    marca character varying(30) NOT NULL,
    caso character varying(300),
    fecha date,
    inventario integer
);
    DROP TABLE public.dispositivo;
       public         heap    postgres    false            �            1259    18118    dispositivo_id_dispo_seq    SEQUENCE     �   CREATE SEQUENCE public.dispositivo_id_dispo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.dispositivo_id_dispo_seq;
       public          postgres    false    218            M           0    0    dispositivo_id_dispo_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.dispositivo_id_dispo_seq OWNED BY public.dispositivo.id_dispo;
          public          postgres    false    219            �            1259    18119    empleado    TABLE     6  CREATE TABLE public.empleado (
    id_empleado integer NOT NULL,
    id_departamento integer,
    nombre character varying(50) NOT NULL,
    direccion character varying(50) NOT NULL,
    colonia character varying(50) NOT NULL,
    ciudad character varying(50) NOT NULL,
    cp character varying(25) NOT NULL,
    rfc character varying(25),
    correo character varying(30),
    sueldo integer,
    nss character varying(15) DEFAULT NULL::character varying,
    imagen bytea,
    username character varying(20),
    contra character varying(100),
    tipo integer
);
    DROP TABLE public.empleado;
       public         heap    postgres    false            �            1259    18125 	   empleado2    TABLE     7  CREATE TABLE public.empleado2 (
    id_empleado integer NOT NULL,
    id_departamento integer,
    nombre character varying(50) NOT NULL,
    direccion character varying(50) NOT NULL,
    colonia character varying(50) NOT NULL,
    ciudad character varying(50) NOT NULL,
    cp character varying(25) NOT NULL,
    rfc character varying(25),
    correo character varying(30),
    sueldo integer,
    nss character varying(15) DEFAULT NULL::character varying,
    imagen bytea,
    username character varying(20),
    contra character varying(100),
    tipo integer
);
    DROP TABLE public.empleado2;
       public         heap    postgres    false            �            1259    18131    empleado2_id_empleado_seq    SEQUENCE     �   CREATE SEQUENCE public.empleado2_id_empleado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.empleado2_id_empleado_seq;
       public          postgres    false    221            N           0    0    empleado2_id_empleado_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.empleado2_id_empleado_seq OWNED BY public.empleado2.id_empleado;
          public          postgres    false    222            �            1259    18132    empleado_id_empleado_seq    SEQUENCE     �   CREATE SEQUENCE public.empleado_id_empleado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.empleado_id_empleado_seq;
       public          postgres    false    220            O           0    0    empleado_id_empleado_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.empleado_id_empleado_seq OWNED BY public.empleado.id_empleado;
          public          postgres    false    223            �            1259    18133    orden    TABLE     0  CREATE TABLE public.orden (
    id_orden integer NOT NULL,
    id_dispo integer NOT NULL,
    id_cliente integer NOT NULL,
    id_departamento integer NOT NULL,
    fechacrea date NOT NULL,
    fechacierre date NOT NULL,
    total double precision DEFAULT 0 NOT NULL,
    partes character varying(300),
    status character varying(30) DEFAULT 'PENDIENTE'::character varying NOT NULL,
    diagnostico character varying(500) DEFAULT NULL::character varying,
    tip_pago character varying(20) DEFAULT NULL::character varying,
    descuentos double precision
);
    DROP TABLE public.orden;
       public         heap    postgres    false            �            1259    18142    orden2    TABLE     �   CREATE TABLE public.orden2 (
    id_orden integer,
    id_dispo integer NOT NULL,
    id_cliente integer NOT NULL,
    total integer DEFAULT 0 NOT NULL,
    descuentos integer
);
    DROP TABLE public.orden2;
       public         heap    postgres    false            �            1259    18146    orden_id_orden_seq    SEQUENCE     �   CREATE SEQUENCE public.orden_id_orden_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.orden_id_orden_seq;
       public          postgres    false    224            P           0    0    orden_id_orden_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.orden_id_orden_seq OWNED BY public.orden.id_orden;
          public          postgres    false    226            �           2604    18147    cliente id_cliente    DEFAULT     x   ALTER TABLE ONLY public.cliente ALTER COLUMN id_cliente SET DEFAULT nextval('public.cliente_id_cliente_seq'::regclass);
 A   ALTER TABLE public.cliente ALTER COLUMN id_cliente DROP DEFAULT;
       public          postgres    false    215    214            �           2604    18148    departamento id_departamento    DEFAULT     �   ALTER TABLE ONLY public.departamento ALTER COLUMN id_departamento SET DEFAULT nextval('public.departamento_id_departamento_seq'::regclass);
 K   ALTER TABLE public.departamento ALTER COLUMN id_departamento DROP DEFAULT;
       public          postgres    false    217    216            �           2604    18149    dispositivo id_dispo    DEFAULT     |   ALTER TABLE ONLY public.dispositivo ALTER COLUMN id_dispo SET DEFAULT nextval('public.dispositivo_id_dispo_seq'::regclass);
 C   ALTER TABLE public.dispositivo ALTER COLUMN id_dispo DROP DEFAULT;
       public          postgres    false    219    218            �           2604    18150    empleado id_empleado    DEFAULT     |   ALTER TABLE ONLY public.empleado ALTER COLUMN id_empleado SET DEFAULT nextval('public.empleado_id_empleado_seq'::regclass);
 C   ALTER TABLE public.empleado ALTER COLUMN id_empleado DROP DEFAULT;
       public          postgres    false    223    220            �           2604    18151    empleado2 id_empleado    DEFAULT     ~   ALTER TABLE ONLY public.empleado2 ALTER COLUMN id_empleado SET DEFAULT nextval('public.empleado2_id_empleado_seq'::regclass);
 D   ALTER TABLE public.empleado2 ALTER COLUMN id_empleado DROP DEFAULT;
       public          postgres    false    222    221            �           2604    18152    orden id_orden    DEFAULT     p   ALTER TABLE ONLY public.orden ALTER COLUMN id_orden SET DEFAULT nextval('public.orden_id_orden_seq'::regclass);
 =   ALTER TABLE public.orden ALTER COLUMN id_orden DROP DEFAULT;
       public          postgres    false    226    224            8          0    18104    cliente 
   TABLE DATA           r   COPY public.cliente (id_cliente, nombre, direccion, colonia, ciudad, cp, correo, telefono, telefono2) FROM stdin;
    public          postgres    false    214   8S       :          0    18108    departamento 
   TABLE DATA           ]   COPY public.departamento (id_departamento, nombre_depa, max_personas, ubicacion) FROM stdin;
    public          postgres    false    216   US       <          0    18112    dispositivo 
   TABLE DATA           �   COPY public.dispositivo (id_dispo, sn, tipo_dis, id_cliente, modelo, estado_fisi, esta_recep, color, marca, caso, fecha, inventario) FROM stdin;
    public          postgres    false    218   rS       >          0    18119    empleado 
   TABLE DATA           �   COPY public.empleado (id_empleado, id_departamento, nombre, direccion, colonia, ciudad, cp, rfc, correo, sueldo, nss, imagen, username, contra, tipo) FROM stdin;
    public          postgres    false    220   �S       ?          0    18125 	   empleado2 
   TABLE DATA           �   COPY public.empleado2 (id_empleado, id_departamento, nombre, direccion, colonia, ciudad, cp, rfc, correo, sueldo, nss, imagen, username, contra, tipo) FROM stdin;
    public          postgres    false    221   �S       B          0    18133    orden 
   TABLE DATA           �   COPY public.orden (id_orden, id_dispo, id_cliente, id_departamento, fechacrea, fechacierre, total, partes, status, diagnostico, tip_pago, descuentos) FROM stdin;
    public          postgres    false    224   �S       C          0    18142    orden2 
   TABLE DATA           S   COPY public.orden2 (id_orden, id_dispo, id_cliente, total, descuentos) FROM stdin;
    public          postgres    false    225   �S       Q           0    0    cliente_id_cliente_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 1, false);
          public          postgres    false    215            R           0    0     departamento_id_departamento_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.departamento_id_departamento_seq', 1, false);
          public          postgres    false    217            S           0    0    dispositivo_id_dispo_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.dispositivo_id_dispo_seq', 1, false);
          public          postgres    false    219            T           0    0    empleado2_id_empleado_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.empleado2_id_empleado_seq', 1, false);
          public          postgres    false    222            U           0    0    empleado_id_empleado_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.empleado_id_empleado_seq', 1, false);
          public          postgres    false    223            V           0    0    orden_id_orden_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.orden_id_orden_seq', 1, false);
          public          postgres    false    226            �           2606    18154    cliente cliente_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);
 >   ALTER TABLE ONLY public.cliente DROP CONSTRAINT cliente_pkey;
       public            postgres    false    214            �           2606    18156    departamento departamento_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.departamento
    ADD CONSTRAINT departamento_pkey PRIMARY KEY (id_departamento);
 H   ALTER TABLE ONLY public.departamento DROP CONSTRAINT departamento_pkey;
       public            postgres    false    216            �           2606    18158    dispositivo dispositivo_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.dispositivo
    ADD CONSTRAINT dispositivo_pkey PRIMARY KEY (id_dispo);
 F   ALTER TABLE ONLY public.dispositivo DROP CONSTRAINT dispositivo_pkey;
       public            postgres    false    218            �           2606    18160    empleado2 empleado2_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.empleado2
    ADD CONSTRAINT empleado2_pkey PRIMARY KEY (id_empleado);
 B   ALTER TABLE ONLY public.empleado2 DROP CONSTRAINT empleado2_pkey;
       public            postgres    false    221            �           2606    18162    empleado empleado_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);
 @   ALTER TABLE ONLY public.empleado DROP CONSTRAINT empleado_pkey;
       public            postgres    false    220            �           2606    18164    orden orden_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_pkey PRIMARY KEY (id_orden);
 :   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_pkey;
       public            postgres    false    224            �           2620    18165    empleado tr_eliminar    TRIGGER     h   CREATE TRIGGER tr_eliminar AFTER DELETE ON public.empleado FOR EACH ROW EXECUTE FUNCTION public.trg1();
 -   DROP TRIGGER tr_eliminar ON public.empleado;
       public          postgres    false    231    220            �           2620    18166    orden tr_guardar    TRIGGER     d   CREATE TRIGGER tr_guardar AFTER INSERT ON public.orden FOR EACH ROW EXECUTE FUNCTION public.trg2();
 )   DROP TRIGGER tr_guardar ON public.orden;
       public          postgres    false    224    232            �           2606    18167 '   dispositivo dispositivo_id_cliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.dispositivo
    ADD CONSTRAINT dispositivo_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);
 Q   ALTER TABLE ONLY public.dispositivo DROP CONSTRAINT dispositivo_id_cliente_fkey;
       public          postgres    false    214    218    3223            �           2606    18172 (   empleado2 empleado2_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empleado2
    ADD CONSTRAINT empleado2_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 R   ALTER TABLE ONLY public.empleado2 DROP CONSTRAINT empleado2_id_departamento_fkey;
       public          postgres    false    216    3225    221            �           2606    18177 &   empleado empleado_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 P   ALTER TABLE ONLY public.empleado DROP CONSTRAINT empleado_id_departamento_fkey;
       public          postgres    false    220    216    3225            �           2606    18182    orden orden_id_cliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);
 E   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_cliente_fkey;
       public          postgres    false    3223    224    214            �           2606    18187     orden orden_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 J   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_departamento_fkey;
       public          postgres    false    3225    216    224            �           2606    18192    orden orden_id_dispo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_dispo_fkey FOREIGN KEY (id_dispo) REFERENCES public.dispositivo(id_dispo);
 C   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_dispo_fkey;
       public          postgres    false    3227    224    218            8      x������ � �      :      x������ � �      <      x������ � �      >      x������ � �      ?      x������ � �      B      x������ � �      C      x������ � �     