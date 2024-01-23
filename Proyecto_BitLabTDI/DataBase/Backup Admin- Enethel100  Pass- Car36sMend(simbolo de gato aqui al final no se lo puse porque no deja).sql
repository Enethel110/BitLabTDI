PGDMP         ;                {            iFixCom    15.2    15.2 >    G           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            H           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            I           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            J           1262    18315    iFixCom    DATABASE     }   CREATE DATABASE "iFixCom" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Spanish_Mexico.1252';
    DROP DATABASE "iFixCom";
                postgres    false            �            1255    18316    hola(integer)    FUNCTION     #  CREATE FUNCTION public.hola(x integer) RETURNS integer
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
       public          postgres    false            �            1255    18317    hola2(integer)    FUNCTION     '  CREATE FUNCTION public.hola2(x integer) RETURNS integer
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
       public          postgres    false            �            1255    18318    tr1()    FUNCTION     �   CREATE FUNCTION public.tr1() RETURNS trigger
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
       public          postgres    false            �            1255    18319    tr2()    FUNCTION     m   CREATE FUNCTION public.tr2() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
begin
	SELECT hola2(1);
end;
$$;
    DROP FUNCTION public.tr2();
       public          postgres    false            �            1255    18320    trg1()    FUNCTION     B  CREATE FUNCTION public.trg1() RETURNS trigger
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
       public          postgres    false            �            1255    18321    trg2()    FUNCTION     �   CREATE FUNCTION public.trg2() RETURNS trigger
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
       public          postgres    false            �            1259    18322    cliente    TABLE     �  CREATE TABLE public.cliente (
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
       public         heap    postgres    false            �            1259    18325    cliente_id_cliente_seq    SEQUENCE     �   CREATE SEQUENCE public.cliente_id_cliente_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 -   DROP SEQUENCE public.cliente_id_cliente_seq;
       public          postgres    false    214            K           0    0    cliente_id_cliente_seq    SEQUENCE OWNED BY     Q   ALTER SEQUENCE public.cliente_id_cliente_seq OWNED BY public.cliente.id_cliente;
          public          postgres    false    215            �            1259    18326    departamento    TABLE     �   CREATE TABLE public.departamento (
    id_departamento integer NOT NULL,
    nombre_depa character varying(50) NOT NULL,
    max_personas integer NOT NULL,
    ubicacion character varying(50) NOT NULL
);
     DROP TABLE public.departamento;
       public         heap    postgres    false            �            1259    18329     departamento_id_departamento_seq    SEQUENCE     �   CREATE SEQUENCE public.departamento_id_departamento_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 7   DROP SEQUENCE public.departamento_id_departamento_seq;
       public          postgres    false    216            L           0    0     departamento_id_departamento_seq    SEQUENCE OWNED BY     e   ALTER SEQUENCE public.departamento_id_departamento_seq OWNED BY public.departamento.id_departamento;
          public          postgres    false    217            �            1259    18330    dispositivo    TABLE     �  CREATE TABLE public.dispositivo (
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
       public         heap    postgres    false            �            1259    18336    dispositivo_id_dispo_seq    SEQUENCE     �   CREATE SEQUENCE public.dispositivo_id_dispo_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.dispositivo_id_dispo_seq;
       public          postgres    false    218            M           0    0    dispositivo_id_dispo_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.dispositivo_id_dispo_seq OWNED BY public.dispositivo.id_dispo;
          public          postgres    false    219            �            1259    18337    empleado    TABLE     6  CREATE TABLE public.empleado (
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
       public         heap    postgres    false            �            1259    18343 	   empleado2    TABLE     7  CREATE TABLE public.empleado2 (
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
       public         heap    postgres    false            �            1259    18349    empleado2_id_empleado_seq    SEQUENCE     �   CREATE SEQUENCE public.empleado2_id_empleado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 0   DROP SEQUENCE public.empleado2_id_empleado_seq;
       public          postgres    false    221            N           0    0    empleado2_id_empleado_seq    SEQUENCE OWNED BY     W   ALTER SEQUENCE public.empleado2_id_empleado_seq OWNED BY public.empleado2.id_empleado;
          public          postgres    false    222            �            1259    18350    empleado_id_empleado_seq    SEQUENCE     �   CREATE SEQUENCE public.empleado_id_empleado_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 /   DROP SEQUENCE public.empleado_id_empleado_seq;
       public          postgres    false    220            O           0    0    empleado_id_empleado_seq    SEQUENCE OWNED BY     U   ALTER SEQUENCE public.empleado_id_empleado_seq OWNED BY public.empleado.id_empleado;
          public          postgres    false    223            �            1259    18351    orden    TABLE     0  CREATE TABLE public.orden (
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
       public         heap    postgres    false            �            1259    18360    orden2    TABLE     �   CREATE TABLE public.orden2 (
    id_orden integer,
    id_dispo integer NOT NULL,
    id_cliente integer NOT NULL,
    total integer DEFAULT 0 NOT NULL,
    descuentos integer
);
    DROP TABLE public.orden2;
       public         heap    postgres    false            �            1259    18364    orden_id_orden_seq    SEQUENCE     �   CREATE SEQUENCE public.orden_id_orden_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.orden_id_orden_seq;
       public          postgres    false    224            P           0    0    orden_id_orden_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.orden_id_orden_seq OWNED BY public.orden.id_orden;
          public          postgres    false    226            �           2604    18415    cliente id_cliente    DEFAULT     x   ALTER TABLE ONLY public.cliente ALTER COLUMN id_cliente SET DEFAULT nextval('public.cliente_id_cliente_seq'::regclass);
 A   ALTER TABLE public.cliente ALTER COLUMN id_cliente DROP DEFAULT;
       public          postgres    false    215    214            �           2604    18416    departamento id_departamento    DEFAULT     �   ALTER TABLE ONLY public.departamento ALTER COLUMN id_departamento SET DEFAULT nextval('public.departamento_id_departamento_seq'::regclass);
 K   ALTER TABLE public.departamento ALTER COLUMN id_departamento DROP DEFAULT;
       public          postgres    false    217    216            �           2604    18417    dispositivo id_dispo    DEFAULT     |   ALTER TABLE ONLY public.dispositivo ALTER COLUMN id_dispo SET DEFAULT nextval('public.dispositivo_id_dispo_seq'::regclass);
 C   ALTER TABLE public.dispositivo ALTER COLUMN id_dispo DROP DEFAULT;
       public          postgres    false    219    218            �           2604    18418    empleado id_empleado    DEFAULT     |   ALTER TABLE ONLY public.empleado ALTER COLUMN id_empleado SET DEFAULT nextval('public.empleado_id_empleado_seq'::regclass);
 C   ALTER TABLE public.empleado ALTER COLUMN id_empleado DROP DEFAULT;
       public          postgres    false    223    220            �           2604    18419    empleado2 id_empleado    DEFAULT     ~   ALTER TABLE ONLY public.empleado2 ALTER COLUMN id_empleado SET DEFAULT nextval('public.empleado2_id_empleado_seq'::regclass);
 D   ALTER TABLE public.empleado2 ALTER COLUMN id_empleado DROP DEFAULT;
       public          postgres    false    222    221            �           2604    18420    orden id_orden    DEFAULT     p   ALTER TABLE ONLY public.orden ALTER COLUMN id_orden SET DEFAULT nextval('public.orden_id_orden_seq'::regclass);
 =   ALTER TABLE public.orden ALTER COLUMN id_orden DROP DEFAULT;
       public          postgres    false    226    224            8          0    18322    cliente 
   TABLE DATA           r   COPY public.cliente (id_cliente, nombre, direccion, colonia, ciudad, cp, correo, telefono, telefono2) FROM stdin;
    public          postgres    false    214   7S       :          0    18326    departamento 
   TABLE DATA           ]   COPY public.departamento (id_departamento, nombre_depa, max_personas, ubicacion) FROM stdin;
    public          postgres    false    216   TS       <          0    18330    dispositivo 
   TABLE DATA           �   COPY public.dispositivo (id_dispo, sn, tipo_dis, id_cliente, modelo, estado_fisi, esta_recep, color, marca, caso, fecha, inventario) FROM stdin;
    public          postgres    false    218   �S       >          0    18337    empleado 
   TABLE DATA           �   COPY public.empleado (id_empleado, id_departamento, nombre, direccion, colonia, ciudad, cp, rfc, correo, sueldo, nss, imagen, username, contra, tipo) FROM stdin;
    public          postgres    false    220   �S       ?          0    18343 	   empleado2 
   TABLE DATA           �   COPY public.empleado2 (id_empleado, id_departamento, nombre, direccion, colonia, ciudad, cp, rfc, correo, sueldo, nss, imagen, username, contra, tipo) FROM stdin;
    public          postgres    false    221   �       B          0    18351    orden 
   TABLE DATA           �   COPY public.orden (id_orden, id_dispo, id_cliente, id_departamento, fechacrea, fechacierre, total, partes, status, diagnostico, tip_pago, descuentos) FROM stdin;
    public          postgres    false    224   4�       C          0    18360    orden2 
   TABLE DATA           S   COPY public.orden2 (id_orden, id_dispo, id_cliente, total, descuentos) FROM stdin;
    public          postgres    false    225   Q�       Q           0    0    cliente_id_cliente_seq    SEQUENCE SET     E   SELECT pg_catalog.setval('public.cliente_id_cliente_seq', 1, false);
          public          postgres    false    215            R           0    0     departamento_id_departamento_seq    SEQUENCE SET     O   SELECT pg_catalog.setval('public.departamento_id_departamento_seq', 1, false);
          public          postgres    false    217            S           0    0    dispositivo_id_dispo_seq    SEQUENCE SET     G   SELECT pg_catalog.setval('public.dispositivo_id_dispo_seq', 1, false);
          public          postgres    false    219            T           0    0    empleado2_id_empleado_seq    SEQUENCE SET     H   SELECT pg_catalog.setval('public.empleado2_id_empleado_seq', 1, false);
          public          postgres    false    222            U           0    0    empleado_id_empleado_seq    SEQUENCE SET     F   SELECT pg_catalog.setval('public.empleado_id_empleado_seq', 1, true);
          public          postgres    false    223            V           0    0    orden_id_orden_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.orden_id_orden_seq', 1, false);
          public          postgres    false    226            �           2606    18372    cliente cliente_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (id_cliente);
 >   ALTER TABLE ONLY public.cliente DROP CONSTRAINT cliente_pkey;
       public            postgres    false    214            �           2606    18374    departamento departamento_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.departamento
    ADD CONSTRAINT departamento_pkey PRIMARY KEY (id_departamento);
 H   ALTER TABLE ONLY public.departamento DROP CONSTRAINT departamento_pkey;
       public            postgres    false    216            �           2606    18376    dispositivo dispositivo_pkey 
   CONSTRAINT     `   ALTER TABLE ONLY public.dispositivo
    ADD CONSTRAINT dispositivo_pkey PRIMARY KEY (id_dispo);
 F   ALTER TABLE ONLY public.dispositivo DROP CONSTRAINT dispositivo_pkey;
       public            postgres    false    218            �           2606    18378    empleado2 empleado2_pkey 
   CONSTRAINT     _   ALTER TABLE ONLY public.empleado2
    ADD CONSTRAINT empleado2_pkey PRIMARY KEY (id_empleado);
 B   ALTER TABLE ONLY public.empleado2 DROP CONSTRAINT empleado2_pkey;
       public            postgres    false    221            �           2606    18380    empleado empleado_pkey 
   CONSTRAINT     ]   ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_pkey PRIMARY KEY (id_empleado);
 @   ALTER TABLE ONLY public.empleado DROP CONSTRAINT empleado_pkey;
       public            postgres    false    220            �           2606    18382    orden orden_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_pkey PRIMARY KEY (id_orden);
 :   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_pkey;
       public            postgres    false    224            �           2620    18383    empleado tr_eliminar    TRIGGER     h   CREATE TRIGGER tr_eliminar AFTER DELETE ON public.empleado FOR EACH ROW EXECUTE FUNCTION public.trg1();
 -   DROP TRIGGER tr_eliminar ON public.empleado;
       public          postgres    false    231    220            �           2620    18384    orden tr_guardar    TRIGGER     d   CREATE TRIGGER tr_guardar AFTER INSERT ON public.orden FOR EACH ROW EXECUTE FUNCTION public.trg2();
 )   DROP TRIGGER tr_guardar ON public.orden;
       public          postgres    false    224    232            �           2606    18385 '   dispositivo dispositivo_id_cliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.dispositivo
    ADD CONSTRAINT dispositivo_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);
 Q   ALTER TABLE ONLY public.dispositivo DROP CONSTRAINT dispositivo_id_cliente_fkey;
       public          postgres    false    214    218    3223            �           2606    18390 (   empleado2 empleado2_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empleado2
    ADD CONSTRAINT empleado2_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 R   ALTER TABLE ONLY public.empleado2 DROP CONSTRAINT empleado2_id_departamento_fkey;
       public          postgres    false    216    3225    221            �           2606    18395 &   empleado empleado_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.empleado
    ADD CONSTRAINT empleado_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 P   ALTER TABLE ONLY public.empleado DROP CONSTRAINT empleado_id_departamento_fkey;
       public          postgres    false    220    3225    216            �           2606    18400    orden orden_id_cliente_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_cliente_fkey FOREIGN KEY (id_cliente) REFERENCES public.cliente(id_cliente);
 E   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_cliente_fkey;
       public          postgres    false    214    224    3223            �           2606    18405     orden orden_id_departamento_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_departamento_fkey FOREIGN KEY (id_departamento) REFERENCES public.departamento(id_departamento);
 J   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_departamento_fkey;
       public          postgres    false    3225    216    224            �           2606    18410    orden orden_id_dispo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.orden
    ADD CONSTRAINT orden_id_dispo_fkey FOREIGN KEY (id_dispo) REFERENCES public.dispositivo(id_dispo);
 C   ALTER TABLE ONLY public.orden DROP CONSTRAINT orden_id_dispo_fkey;
       public          postgres    false    224    218    3227            8      x������ � �      :   j   x�3�JM.-*�/V�(�M��/�44�H�+ITp�)I�2*(H,JL���K-�4J� d�9�s2�3�9�QeL@���q�d���L9�K�������qqq �@+�      <      x������ � �      >      x���ۮe9����wQW ����`hI��G�N��h�5�z?g�_U��Z����+暓��6���������_�w���������?�?����o�������������?�w��o����������뿛���������������������e��������W����������c���)��o�����?������������������y��_Ŝ޿����zΪ�l���ӗZ��������yx���ȗߓ�������S��=���P�W�W���y&oUB
�_���W�������0����������mo[h�+>�z+�����������O�s���[yOޟ�����9,�	��g���F���-~����W%^5�ŇW����p������O�?���L������i������
'?lW�f��o��Vŧ������:5���|8�c�u�>i�����W{y{��϶��|s�9�\k�3Z���U�7��w�%�F	���'-~�zb��W2�����p�o�)+KHN����g��i;�[��<`�tZa��8-��s}�����������{�y��1g�;�g���5���[J��vOm��]��/�|��a���?w-i������3���F����*N�O��?'�ϗ�Ν:6h3�}���:����5Ɯg���8��KKy���<��x�,m�1��+��b+5��c���W���r�k|�ȡ���=���j>|�+|U�{X�6Σu^�/����cc��,�~���l<�;�[��Φ�0ֈ�V��Fʥ�<G���3��v�	�K��y�&b�/���L{���n^U�2�G�U<��'J�b��f׆��r�ߒ�!��9���������Ր�ʳ�Q�{����0[߹�1�˓�rfς�ʵVT��g�g�F_�#�<Q��)C-��t�iy�����[��=���*���GD��g~|��������S�(<H�;3G��O;e��e̸Yu+_�k��G�%��˾��k��f>c�SH=>��Ě_~O�9��|����Y���8�y�z��bhr����_~�@���d]+���WA9��K�,���9y�2O�c�O�}N٨�p�����K}c�;�����m5����K�xg�s������������V�(�ՉP�_+M��!K��{u��2����1�8�1��h �=�>B�6ص����ż~/�Y1���m4���/���'��Q��1GQ�=��ecR�u��p~X�3΁��XQ��,O۸���~��/M�:��p�|���L~������ M'WK-�t���rx�� m4xc�1���^"��)���򆒱�-OT�A���e�L �G8�x?���	�g���>����i�I�0c��l'��>�fEk���B�yF[����U���X3\�/�d�ρ��ɦ�~Yz^��o>o��ƇS�5����D,���2p#_�0H����|Z5O+��4Zx:[��1�z�;����{y��}9�x�7��Ɵ�ܳ'<h/،�c2bD[=�̾�W��]�����n�ڻ�S<�	/�\�_v�^����N<x�5��c������1�~�+)��ः٭خ��
�8��p~�q��+�;����<b�8���1{ɱƎXV�*�����P�ڨ���������vKR+z|�	��3����/���D��+��s��������1�79��%$g&X}���� ��1�Bx�ƫf�&F$�����=���W�|���֙�Žm;������#D������p�-]?������߽U���"9�^K3�e.�qZ�s�d9��YG�A�_{� �`�������g#��~���C�'���H�>����n���@�<_H��b='��Oz;�����B8���R�	��Y�������&��PM/����L�2v�c��l��L�!;<NPe`�jb���
<@�"��3�طx�Nz���;(�E�_�����$�q+#!F�ና���1Di��=v`�9p"�M�|Q,ve�o!4�&衎T0���5��C�p`���o�jz��P���糾�tMU��e�Ԇ'���}�i빘���?��w��<09�v��V��Ήԭ�rDR	��U�^h�qu������W��w�� �|��ÏU|�`���x�]*�6Q��v�n4��0����Z��M��5�@_�P�8�2׺�,�,��ߞ^�{�[D~9����R�JCn���84�ψ�Y����z�wr�u�.�b�+��N�=7=��|b��dse�6���ф~����E�����e��g���?�3%�޳9à	W$EC�����e d(;�F��F&^< [xP���oi��W
�ʋ�C�P���H#J���Sl�� [����A8Jx3O�
Br��#����y��Ga�y~p��P, .C�[WO�����瑯��~�����%����LW���F�8Ik����X@A����N��0�����7^���;�XH����(}M�XKL�3"�K`�|͡6\��D�gF�0@K���NF�U@��e��y߅��h��
z�>��?�A��FT��=�P� ��&�_<�Q2��q�RP�S�!�a%����!���-�]L5�� 8A�ëa�l��h����ʀ�?W�_ �o�[T��3d$?�s�T/��q^nT�� W+S_Oj�_�?�=c�V�V��Ĕ��S�k��V?�0�J_9��7[��W�Z ��� �,�h*��q5*�����]m��V�qٸe�m}��N]��Hs��>�����x��q ��Fں|W���Ihq��� ��o�"�t����3K�$��F� 
����"pUuu���4qH'b^��H)�:|y��*}z� �]>2��Q?�y\���;�œ�K�Y$��<�G�T F� ��X4P�Y�P�>/��@˥���x��_"ti%JUJa��St P�Y%�Ƌ�@�2+�<N	���g1����*t/X���a��>�s��P��ԇ>��e�0����Di�VH�P9�� r0�bW@�H	 p�=Ps��a���I�-&.A��k�m�K��ؠ��^�s�/�r�!�,��&����9�x�`W!<�l#���IxpD��p<�⊒�\�~�=���<��,��t<J�\	�%y�9<d�/�Y� u�x�4����L��{~Q&�C�º��HE�@��5j��্?�fbM�a� Z��Ol$��c᭘=p!�
��6��uq�\��鎀�������*��M��C�&� ��i����|�s#`�+��y�?ԅ�k	�!O[�b���M�EF`�m�4kL;����C|������á�h���>�C�1�5�T[54\6"ǳ|��L��fp�b���,�*xBH�q��\BvaY�?W��f��t�^C<]l@���A���e��yx%p&�3��S��_������������/��$�6�Mƍ��\Q<8m�R.N�a�|���şpJ�6�?�&�!8�(@�Mv^߮G�������wQn5־��uO��8v�°��@r1^���@b�n�\Y լ:��<�1���@�1*T*��n,���a�b��QFЩ��)�����Fq"���%�Yl�B �`�g�o>���lT.��#����.S���I�,�	�����Y
�t���J���!Y�1�G$����xaN�3xɧV�X��!B�8ɨ)"��S�#l�o�����ӛ��s���쀾�����p� ��><7,�u1&'�B �ƈ���?�<U�p��9}z���)��bq�;�Tz�5I;�._C�?u4t�W�g�+�0  �)�ao Hgkah�jST���*�む1��np���CY��`,( ��l�"��0��#�E@���B���Ha ���p�fL�0<������Al%	��#�ƭ���Q&��vPO���4����    ���΃O��o8�D��c���o�.���g��A�����V!��s��a����6><�Uorq\��I��$�����qd@|���@ܞ�Sj�B���X�_��
������xk°���u>�¿�2�b*�]zh��?(�ֻ����Ϥ��p��)�F����T��	C	l�y�jV>���|����~FO�v�2K�_�(�-�q1��n����õO�����A `l���_��u=3�G�ba�س�3ƾ �U��|D�1挸�b��
�T�Ǭ�!+S5�,�l0&���l秫�7���#��F��ʱ3���l��v�񌯤��f��}2�b��h�fh�6A�Ѳ�E#]�n��T�Ǉ��J#�k�Clֆ��������O��D�g���b�<:(-J؏�tHj�Fq�ň�͎}H�5��L�'��\� ckZ!�X���ɦ։G��b�0Ef������J�16�<ߜ 9�)�ث+�H�'�ՉO�y�Q�3������7�}�s�)� �BOWV_ر�N[	tN�r�RF�H����u� 8y^{0��L��� ��詡!�10��jm� ���#��)��� �#@ik�<��<�0v�ʞJʽz`�����<ľ�?0s��ѧ��B�G\@� �z��(�v��a��D����e1��&H���-���Cm�Wu\P\���/��F�YwӰ�l�.a��xwPjWA��F�=�G$��`���7��X�)��CK�?�+
jy��+��<7����8�8���@�Q#|>hX�$[���n��]X�����8�p""<���ѱoF�'�!�r�dY,x@�0
� 8 m5����]ͭ�����`.�Aq4K�Q1�����s2��[
Ϲ����{@^d��؂z�Zo4b�jndB:���(U?g4�\��q퍧*XX�啲b��
�Q�;�}g=�C�A�895�3��}�V�fnv:��hc�k��4=]��w�a��w���!U/���<m��y��hz-�����G6_��ݜ5�f$(XB"��ߊ� u�'���i��p���O��M���������4�=�$d����5���>��P��_�Ɵ���{+8�$�?���5��h*�b�`�����t���d���}a���j@�/<�|:�mK��	+�ٍ��������2p�X�����\�hʰ�[ ��1�D|˖`m_0� �v0���o����A`9��׃��<`�T���!D�<>�8d��2�(�������/MlJ��4K�i��!n_�h�F^�p��і10�LxYL(R�u�,i���e��/�2f��'㝞���o1��,}a�^>�DP@���� %{���ʨJ��CR`� c��ьʆDS� mX�Gش����,�0���:�c����&��"����70�A%E��Ȱ�.��{vƾO�N3��{(�oɣa�sӆ�w ����,\R c�(�P��и�4�A)�eC	�'J`ao\`X�A�7ʛ0�o�"�ͤ���c��l��>s�/L1X`��FC�?}����Q�J����(��-� $��iA>5#&[>���ؙC�q(���?�7@r�o2k�!mƋ�&����I1 86�b���}���<��ӫ��|�+3a $�-�w��rRdx�����������ο�����3�c�`�r3��ϰs��A?*���7�.�m���x�;u�	p��B�W�t�q�b�b�r���Q&���lR�������� ���O�0ng°x ��޼��u����g���O*P�A�F��$	��I�����GX����-B�
FB�߱�<�q��aU������>��{�H�i�<��牬��A�7.?���pa�T��� 'V�������)�ͰX�R�.f�6B�� =a���r��2Ќ�<(Q׫�f�ٚ-� ILl.�*�ˊ+����@������t_!�)�g��|0m��
�l���W���5���c�MNKW���qֺ����BQ�U=�@�Iî � 7+�8���l���6���>O�k��G�p��������!d������z6� ^�"��]�\�v[��-�N��5��`��|��%	R�)�RܑkE�ycwa�n����c��,��,`�OE� 1��8���z��j������U�@t���� ��Ӳ҉19Z��y8v �3���aI���t�֠�� ��-\o�*�0j9�klW��`����0�BA�M`g��vU5�t�e�R���蠅����`��F���"Y��ϯ.؉�cLp� PpG�р-y0��e��i����>3�'o�d�.�������������bϧ�=(RFb�~�^!���-�c���Pe$H�b��Y�,6��/���eh��$_��t)��5��@b,40��X���IZ!ˆa�ry,Ç1`8��-]:�y���ӄ�ۂ�d]���.��pz����5C�y��q��V���g�qI��u����8Z\:�80�3�1LDnNX00{؎X�ya��cg�=8���3��Q��b��'��D�#��0.��g�2� �~o��JςV� 
�O�@(�xU��U�a�%�Ǟ�Txl�cA&�c�n��`r������sB��X�ƀm�8�-N,Xw�*����iYm\��e�wE8Bw�~��xR )<�d����ð(r|��a׻#d��.=�y����;�G�A.kZ���_��*s�l�)x:�E�����	 �w5��!����B�fm��z':��`	��u����.0%��-Z�P���_c�����gq?aըA�x���<A�wv��7,@p�}3���T�z��s���+�SpA9�=h"("�����i(�Q�k�js"Z;F���t��Bp�PV�l!\�U��+X�VU$쓅�8`,���]tSFՀdH������Hek��� ��HF`u��۰ʠ%J^����7���j��bމ��Ȃ�)[vr]r6A�ac��<̛Bf���j4O8g���(د���%2zs�r+�Q}N�'?a���R�<0qʂ��#g����F�"�I���U~}��MAJ�&"����
��I������	@jɍ�-�^j�z���48�fz�s0|O����"��^0|ٺ�`�@��U��A 4�6$z{�"��[`9�B[8�^�v��A� ��Q��oE"��α����<�`�� Q��D�C`���_���`�]�t�����)� �5���l�
��6��Q-������`����w^Fv�ů[�!���Nɵ��A��C0f��#��!BS�S��4^ä
�pg�u�6����j0wкm�Pf�,K�4/�i��:�p�w2�ll��c:�<��xw�Є4J�7 �'5$�a�1n�㚃SƦ�,z�P\����x|1�Ȫ@�"���,�X�?~����Z�d���z,��� ����疗�+0�B��HS��։[�\ [+Y��"#B���f��M����^�i��W1yl��;U���Y|�Y�
�Oqb�P�Z�5�­x�"�°˙�kf!�S1�I�>w�/�Ł1�����?E�����b�s��� t���k�q�"�$�m3��Eۄ��Z����C�h��j�lQ��s2���=[n��3�n9�����M�W������/��'�����,I�7��y&��*�;/��k%)��R��ζ8�Ȳ����R��C8@���̲,��8��'�*Z^6����-C��rl��]��K?
l�b�xAA���HZ4݅H](���|��e�.CˊA[�BP�&��>7��I01�֭C��P�g�)�b�'��Ш:�%��]]?8�����K�(p�oR��S�/lW��]=����K��M���[nDD��'��i��� )Ƶ_�'���j����F��c��L�x� ��5ۤ�C�m�4    b��
�#�xO9�2�6
�j!��xY�,�¾x0/�|���Iժv&��u�h�2�t�ֆb�:�.����F�q(���L��)y������!=��0/H9���<8Ɛ����bKo�jVԸYX���� �ÍT��Ӳ��E��IVf�k��y1Lw�l�2P�P�������t�K��P��MʳkML�Qwg�8~?l!��.��J@\"��YFE�(�.e���dJdmtC�	�i��٠o��#��>p�[�z{�zy�8p��V�f��
 �K>��fG-׼PD���6�_�m�ׂRvf�Sm#����}��h������3��G�2�zp�[���Z� G��Y/ӷ,�o4oۏ�c{���4/�r�l�*Ӫ�
�����#�b�{c�W���D>"\f~�-`�Xp:�Q}ch&�Ҳ�[�F�8��� ��΅�`�h�)P�����Ӗ�`<�v�E������Li9�x������	3�a[ �*�l5��"��Y��������c}�o�%�������o6g�Z��Ŷ4'#clR�0��F�jyj��),��y��bS��&�-Z��H�&���*&{�h���fAxyh<�8(k���JG�sx�jWvQ�N���U��a�4����	"Fr�	]\hpc)3�����%9x` �m��{뎠c�V(9d�Pa��Y�ekh�}:B���M�_v�[������iJ�ͨVc�R܄m�y��<B��V��c3a�H�7/�{��X����9���d������ve
!@c7Gh~�[ݴO��Z)�Ə�F L�a6��.ou�0���!/H��e��4f����g���)!����~��rR�a��=��;��m����$�㈱�l8�
�,!{0�ej��r�q���_�u�	`l�K��f�C�хr�0��0��X��F���� e�)�Fd>UT:D�����5�=i�[�_8��Ҷ�y3�����9���ڏ͑���'? ���`3��i<�}�fa������|@�:ӭ�\�_4-ݤ=T��v?�S��&��t6M�����e�O�Y,?�P��D}m܃%c)�#�c"�1g�R`���5��샜H{�Z�)��5Z�̞oY���Id����L����<����������oĩ��F��~m�Դ�to�]7���/����~������?՝,r01��y�*��-��g����o�JQK�U�u!�>�	�n�T��vd�-hܯ�Z���h��1.:q���O?p��|�6�fW����mh�.ێ<p�$�1>�@Dm�2�ª��U��k48���h�8 �t�Uۈ�ۻc��a�կ�Y �U��|��n����-��IV���6ty��-؏��3T�?�=>j��rO�i�D�٭d;�=�&]@���ȏ�(�6��aFk[gm�c�/F+��f��}+N�Θr���.�,7���ɒ��`���Rڃ>C"��&w�c@������zkE�>�m�-���@8��T�m�ځ����ل�x�]��UW�@#<ɱ���R�E}����C0}l0��,��ڃ���BR����Hx�L{�g���|��W�2��F
�"c��%�;Kjα4���e��{4}��4�b_0k��m�Xq�8^������0;�c��6�X��r��Y i����D"j1�e%<x>�m;o�;Č�%��hD����"a��Έ�V<�who�*wӧzpɝ����.4�_�\S�N_<�<��/m�3/���33��x��*���X�m�_/�I�O<'E��g�x��C� o��0�&�����Ϩ�=�n��Z�S�ہ5&�ƱzXV�78ՠ	�l3������3qE@��t��Z���|@��}@�?�8�M��;*x���w�܆K�=y�?�5�?�$ﯿ�O=�%V�_I�m�	��Ħm�h�>0z���e� ��_x7���20�]���3*�/kL>�4�,h8f47W#�haK�8kb9��r4���f�@�ڱ�˄��v�A��`�͵#�0�j4�B��88����sX95�R������ 8��x�q�\4|_���X��uK��K�Vrd��I��<�EgV|�+ ��$�&�͏
<����p�s�r���2v�T�8��Aʿ	X*,��u�V� 7�VR`>߰r��FtO3�.b�A�@��Yʘ�l2���v�ϟz�IV0���h}�:�K��i:��c�yl
K�<o���Z������d�����S$p�x@fl/��o��� /h"�\݊���_�?F�p*���q�#�&ǋ�]�ƺ��c)��a��@����u���0�c3�����͂>ၿ��BN�{�W�	XD���Pv�=Ò�^Ga�Ѓ�������,��~m�d�ٮts�{X��~�����a��,�U�X�4���눱�P;�b��]��	�ӱ��0�5���ˠv�"�n@S7�*�4��r�=@&��{���)p�k�&���.ɶ�E��|;��|x��c�8	��g�he�>ؙ�1W�{d�'hNbT��?������5n��;��"#�b���1$b�n>W��H)�tr׬+�G�7�pԠ�a�уi@d�@0���A�S:s�O�X�2��-#��6�����1T�F�>� �\��u�ύ�Zrv3>�� 4�Gg`��6��y����z[��Y3�
�c�[[�����B=���UJ�I���oD��b�Y��9�zĞ��c�,���N��$��>h~��Z��T�����E5X��.;L�Rك�5w�R���p�f�V�q�ؾ�[cIwi��a��qJ��h&�$*��}-z�����7$�?�0V���RA���<`�#T5�c8{i���z�Y���c����0��i!t���t��Nx��5.y���Nu(�����u/�-�l�,�xJVxƈZ��kٻ�]��^�Z��4�e(�h��m	�g'��0^M�U�6'd��S죲9L���)V�J�@VU�q%��q����z}v&[���;}vJ��	�ytё��bM8�"u z��Z�����v�V��e0�]����M��<���3%�3-�C_"�`��!���~}O��+�f�<�g(�vdf�)/0�5�n2ԒO���D>؁错�kY0 	�7�v�Y��{(B��m�����Q|o�	8��~7�>X��Z)f��=���l��xm���!cb�oY�o��?��F���rF�"���9t������Ů�m� �aq7�e����붭Z+�7	��8�����#`<�ܾdD-�^!���X|�n*d���ֵ������&>�h�9k����x$��7 �`,L��.%��������VBpHf��'@��'6���O �B��Sռxx��ck�!�a13B��R�|�S!?�`u�s�¨{y,��&�63��J�y� n�9���Q�k��K+M���~�Z.�v�ǓC9p���␟a}B[d�<�aQX�i�G���r�����P�����F�;��[���>�u��^��{�.��R6�Xò��ii,;�c��L����70��{� 7��dE���f* ٌ�9�m��XEm���ݫ6�-�z�U���Ah�m��xH6�E�h]-���Jp��u�M~'4��}7��PS:@3����<�e�X��ogI�|�sx2X�n���U��_��g�s�b:	"�2�p�	��!�w`&��ݥ��7f'o��a������u��!F�{_H0)oR�m���E3O h���k��U�*۽ �9XCv��6�쪶�;a���6�m� ̠��fٳCal�A�˻������͵��`�ֹ�647�>~��H �F�Zi��x13߶�oڲ�s��S��[����4�fG�탦M+ʭ��wi7���6S��st��	��_�1���j55��1Aѷ� F��kry �)N��{��V{�ZL�6�m���l�oi?��ݬ����d��7<�-|�J�D��|���	����<Q{�=pb���&��P�    W�9���9{�qMۿ��T"[�k�>Oѧ�h\�V��J��8⼿`�G1�>��aX�U]��l�v��d�s�Lc 1��	a������t
��=.o9'F���K\>hiy�gm(����(/����,�k���\�b�Z��]��3Bc����=qG�L駋:��k���,�X1: rt��AǰJ�Z����r�f���� ������.|�<FG�Y�@4�+��r�V~ܲ�g��;󁭊6����-09�XQ����'||j[�?�Z�=�)�A�:����&�����O�g��,Q���v�P��W����_������El� ���
�����a@��|c.�u	Vs!�X�[����FY�kV��~�n?�4�C�v�3m
����v����������ϑ5ۚC�����]U���w�ԇ9�V҆"/�����mQ��P%��C��� R7�x�h�{��tu/��,[��e���ި,����c�1}n�k_�!qo�c!�� �ﰍb����qX�࠰� 5@��%�	i���1<fuU���v����fA�N��k���i�|���ʂR�5wpU��W�l,#:�tiQ�w�q~��_�:�����vtj��:�o�1����f��Yg�X-�ֻ���� 8,"٪tm'_�N�O����k�vX%��< ��og[���[dnev�`�ٱ�OM� f9��>Na��h�+8�ē?�r %Ux[���2�B7��b��m����6F��t\\M0��8u����A��hlG����m���g>�#�l?�����ZX��SxCا����;�*n�rqV	F�읍�9�>�7{?�v�UҒ�4y;�����k	���A	�l��篊���Mǁ,[=��-��~A<	y_��7��S�l_e�v�[d*�����W�> ���s_1ǹ߅\5ÒH��W��oGG$)FI�!�xm�r��gOr�.��� ���O��|dga���!�p�Z̓3��Ѣ���4SP�߳j<7��N�<H�4BM��	�&��lu� �=�s;t]�aGݎc��"�c��~1]�4@iG�#����%���^���@A��W^[�^�y���iƾA��Ϡ~�������k+85x�AL�5|�w���!���4���/B�`��*�g�uB��1u5����p'��~�eN#��N(,.>�mp˛��-&���{���66l%�ώ%d�W���n�:gd�q=��xt�N�h˼�ݴ��-{,Ǐ��0[k�����ŏ8M���˒ҳpb�jiV����'��ǩ�f���):� �T�s�N��M�&��.�y�{���.��a�ٖ2��~1ArK�p��I�y���0�XYŵ�7@|c\C�bL��U��N�Rɾn�V��i�� �ۖ?=;֭�4b������,��0j�J�;���#ò;��h�c�0]�j��S��Q�	���fV�s��8�T���OkW�����\8c����-�׭��1�o�w�ƞw;@��~XI��/�R0l�u��Z
�)���j&����\�l�Дyv�T��:�e`�l�A
W@$��a����~B��`#3�W��X�Zr'�,��茂�r��{&�;j:���|s5Ne�}��pO@n�&$8譀��!`��T��6U��cǂ%[�Y(��#�KA�I�a�������خ"daOv2?����c��9�!;+�t�]}ß���<}���-g�!)�n��Y�m���;U�9�&yXaK�#����,·���Z0��ly`�r�RqbX��}kfa���B�؎�[�����z%Z0�V��o�s��m�b�D�h�#H�p��{J��ބPe�t�:���}N\���J�Hƶ����f� ���Î;\$ؽ�LL�W�~��8�X�z�B�x=<�~��!U7��2��Js
��ܹπ:��~U@\Lx6:B�������9 ��ð[���b��q*�g�]�<����� ��,ֻl�sNrp��,��ዡ���e�ʇ��q�Ʈ/���6 K=��/�:R�`Y��~�%h���0��T�o�����; �����*���lW=��������U�`�xLHX�
�1��������߼��~���������gaG�]�r���n��s���c��3�0��Q!�iM�%�8��8�44�������K���E?�t�����8�ﳸ���m�6�bŽ�&�GL�Z:��K��j�<`@�:���.Jq#[�mF��+݉�<D�a�X�#&�с�ҧΛ֥�Cv���\��`��չ;HqYY�k���y�k�P_;p�a1�|�0���@i��ro�qpdѺ T e�6W���S@��V�68Kv�3b�,ġb��b�������>i��#�v�;:�;��b�<��`̉ɔjGUk@���2G��}���G��,��A����c����� ��y u�zgx�;ր+��V��EB6p�����K��~ç�N6nz,��˂�(b&�t�zs��@<�N�D����[�3)�ҟ�`�+ΎZ'�r�H��0Gb�y���N�{���:' ��,������Yj�"&W,�O"��U(���?8��������D5�'*�5;FX���m�yxaa����IÏ7Z,`�U^��e�E)UB��b1E��c}^&�f۽�	�[��f}��YG$n�Bj
}�Cq�5�C��	޽an:�,�wpN�Y0UK>8����s �׏�;�\��@��-'w�n�TC$�^#���c.ŉ~��a��7�_ÅK����W��i+}�ۿPs{?o�7g����8zG���mWOa�r�]i� �����9��b�b+ʷ�$P_�AM<�ԝ��[4 ��b{a/n�J��u/:V�����`*<������󝓖5���5�!�v�h�@�Ƃ?Q=�%q1B?��	����1Ob���LvFYdCD:ENw�v�7|M!%�,	]�#�^��w"��A}	�rqlj�,c���m6&9��Ćα�O1��|5���l\�)us���P���P��,ޱx�W�P/(?6$��r�xd�G���!шS���M�8��}h������)N?J�P�!)��������qN3��u}y,���+o���&�+FK��]��3�P6�A�K��[էxp�!�u�u��iߜp�8ow�p_��e��[o�Dբ#�&��h
�fJL���6�r�_.��Y�R�nO�Ӵ�G7�a��t�ia�l�ɼ2V��Z5\���z��@������"}�0�F�D��R �g��c�GN(cvKX��oGf`��G���½�
ֳK�LG���".�"b/��J�e�.�g���b,灱�2�ymB�졼@ f=�T��p�����G"�v��呎�=[ώi��✝!�X����Rh�R�~�Ǧ_����X�缺"XY�e��R%S踒Y�#�v:�J/�.Ьf��vF��9W��=P����:QeMI�Ê�Î�!���p��B��w�}#��>���?�-��^]�^��  &`XM�� ms 3;L�r(oI� �;
�5Bܽ�����He��_����c�m[�I4觹E�ܙ &��͂��$��m̳��ď����֚n\����>"�]8�%��9��m�$Jpq>��Z�îN� �v���Zs&�s�vP��ֱ�����<���4F:��4�G_����ܱWݬtz�n7>�A���Z�$(�����	����Smh��RށW����x6�f��ai�O���y�PGx����R�g8<>��0��*� ��NG/Ɲ���e�쨢"b��*|��r�C|X`E�;�����A���&��o���u�d�{�@4G!�8	G�+shf�[���ʫ�ӌ~�1��y��~����n���`g=���� w����ٱ��I^���;`Ľ`б	j��^�q���H�TmG�?n�sc�E�Xr2粟�\V�����
˯���Ӓ��    �s�{��mH m���	��ױ˩9.��T�;P��LE�QN�1��0r�0��[���}�kxJ��)�&���qd��`��R"z����ۜ*����������6�l�i2�ߜ)�}�%6�8o�~�y�l�7"fzޡ^Ӿ���	�Sxl�(��f_�R����|�}	6� ���v@���;�xٹ�1��[^w�Zk�Z�} �[}�
�p�:U�$���%&���L�u�|��X�6������sx�%�O�r�]��E��J��8�br��c�M�(�; �g�g�����'$�M)?)���� ���麎RTu��� ������L��e���'ɣ��ש8N�y�`�p���Ld`�ƭG���`��k��Pu����D\���|�]0��y�����0�^R������oS���F"���2�t/p�UC��O��*rH��(R3�k ���Qk�%6�����/�9�� ��R1��+�r��]��������eEM̶\8=Y��QFBp�|�1��E��gYIv���t��-t�ݴ@Ƽ�OdE��f=��8���!��� ����G�u^Vb�2����3L�0C-*u�7އo� �^�ZpO�\xéeZlb�c"w�/�.�1��Ƞr����	�p��ۛ`�����c8b�vn�ۊ�lh8�X|�h3��$������>����i�Q�ݙ� �-GpUsI��1��r[b@���׵�_4�[t�76��`96��!pRhc�,\��.��ʷ�#�{����N�	rE���~,4���ю����"�[��!b�yn�����jހ�ymX�:zB0|3ϊj�o<�W�d���ޜu��Q�e%,�cr��@,�4Ca�x8��X�w���b[�������h�0�^��sCz!a��̞a`�2��%C��~4 �D���oJ�k`pl��yP��`ub����P0$̚]�s��u�7�u�� 6�b
��ͻF����}`�l��=7���� �(��Wr���@φ=�^wu��w*��k�l��/\�J���g����6�����wh���|�i|Vt,�����c���b��8�Gf����4磎��e.�� Q�)�j���!���p� v,�כ܋��W\4�j�#o���S���a��1	4�
��q����e�Dr���"m���Ɠ�H�5.�+N붶rݿחxg����0z9���¯R-�1-Gs:X���bd��d���=Jw���<�$����r�7�ƹY��vl�`S�L�-�� �$��U��b�:����CH��Q�$A����y̵�z�"��9�| �0�Ύq�p5���5�{�2G,k)��!�/h�81FcF<�@^�t���sZ.��=����δ�������X"6����0P7>d<lc�Y�ׁ�z��E�2xvH��|O#Q��Y �#~_���?Y����Y�bt�T4��>��&�!:���'�cSs��U�����J<��18�+;W�^��LZ��,�C��(�Ҝ��:���#��d�rpV�km�-�X�[^�?��y�<s0S�u�v,ѽ�K��dP�c���T8�I@�Jj�i��1:���W:��E��wN�:	��7(����T�4�es����v����1�ڱU�>�A���� ϣu���V���ǁ��TN>��"�;��[g��YC$�Fг2���o����y%���PU��|��뱶��w���X�;e�_�[�i��n�h��t�Q�UrZuOv�$�.њa�n��7o��zy
��e�@r���1�wv�(�t�`-vf�wat��(0�z{�F�������YA|�8,��a'iK��o|:+fW���+q���<ݎ�nW3d�����ɏ7�}�v�N'x��ԉ�b6.�VrE�˲,���ӹ%8/��h{&L���`b`�I(vQ����+�ΔE2+�q��R�޴w�-�0�o~ǐ���O.9��⚑8�]�%����D�xm�1�����OT;�p�����ި%�����Hv���.�ȈܟfƢzmS�w����L�#r��H��0Mߨ�Gz8�����=��Do�|YS3�2�\��N�� �H:8�	~x�cpl�)��:�;ھ&��b�n��]V+X?��E6�x����6cr�W��"s�>��#���6��?B]S%����P˝�kQ�d�^��ά�U��4ߒŌh��c��و�Ep��p8u,�n�*,��,�M	;Vh�spG�ӳyiǞ�Zgqѣɾs�_S:
�c��{5yWf��5��,Ẅ�f}��;I�� �e}������v�=Z/��^��K��;g�>�%�'�;��{p֊�'(=�[dd���<ͬ^=�=�ڣ���u����*�؏��>l�5��nK��T2�6(U��-���
����E����<V�Rm�+�7؀0]i!L��5�h=���1y�}��7�a,1zݍiB����-4�1a����#���P��� �2��S��� 1�kg�����Ky�:$���������eW�u���/O�_x��kq�:�.m�ż�9u�cB�!C%S�)�����|��)vh���^L�1��8��i逷)y	�iW�f�#`zyy����9{��_�&�̢��ScXp��h��u���8�Ҭ�XP�������c�}�����$��w>F�y�M���ZN�.Hmx����]2b��)-��⍓��Vo�(�7�d�&{)���kK[�S�j]���SLgV�;k챆Wt,��4Wk���VR��8���.!��H�xE	�����f��)V�|N 꽠�f%͵l6ao[��5]�Q��R��?�+/}�?�9���h��]��W�<�	u�r��/�h���P�����[d���r�%� �7�ĝ��k*��%��Ř*��X��Z$ͼ�zp�4�4%LF,��0�^i�Y�ߍ:?�V6`�/?N0�8�:B��uWn��?�E����:��]1C�=����Z��{�������&n���Y�a%{v@��m���Q���r���}հ�6�xw��vr���n�4�&�8ś���^�WG����O�!�=U��'oW��w��p���p�������%�����	��,�h7�sT��Y!�+x���T�ߗ~uߜ�g�����q6�W�Y���7�v��w^��D�PѾ�}e*�w8���O�_�7��2I������툲u����ٲ"��Q������m�,�
Ҋcoٵ��}��5���;�7,�KU��\3�,��͔�d�����,�~�VZ�S#�^Ɯ�����\����.T�j/�Z�;<�c��2��j��f�L�O�\֝6���E6D�<��k���2����l՜9�xg��~�-��:?�}��R�8��u�m0l53�X>����Y�XL���Z�c�$�g�>o�qv�T�-�a��pJ��y뇭�Nw��6�ǌ�h��q�ިb��96]Fq�a�"Z�xY�x�|nb�j��W?�����Vn�B����l�r���0�j�r�%'iI�~n]?��+Ne��3k�����9��R~|�lH����fMtK������	�Ut~��A(�8�X70�-�<ò���}�?x����߿|����8��9�Cqh�m�,�<� �SU��u���1:��(Xk��x�8��4��j:D ��#�BM&����7K~�|`��hn��g�?6!�Tm��qm���M�c�5|����/a��v��ْ-S�?��tv�b�z��M�[mq;��-�p/��X�9���[b�ڬȒ�g�/ݳ����G�F/�\Qw2�QL�r���!������#��jk��Ȃ�!�����:�WXyo�0��09�HE��-��#J'ḲK6�K��շ��[�y ��J�$�!�@)a	���7����l��IL��d��h��n�|�֛���eo�ζ�D��;�	6k]֓9h�Ɨ��̾��1{��
ʯ��L�`����B+O�t�����t�x�Dk�}%    ��[�̫���r����y$o�ϟ����	k*"ıڻԬ	{&�A��-�v(��}�nK�n��8� h̵:�����F��K��CK�t/>4���o���������ג���-�k��,��g���+��K]�~�e���h{���#�X��;b�c��l�_F�l~���{�~����x��ސ)���{o��L��{�����t���l���С�>"��z؀��mF�>���ނ
� ����?����4E��t�9��[^�ƞI��t�N>O����_V^L��_�z��(��׭�n���TJ���1�ۋa�Tsv�r��w`}���~�&�4^6��܇O���h�ْ.��ӆPƽ��w�f�d�����&M�P�\ Vrv��-�o^k�|�'{'���g��Ϯo\�`ֻ�sl�����sy~Z�!�nv���:���՟��v��s u��s�\�W�xA)��˷�to��yM�0��G�U��no�7:5�φ���>��ۯ����_ܥC�����R �j��#w�(}�z�����ptn?Ip�Xa��[�������@Mi�?����Zw����,�jɆ�������L��8���w?0��߶��߻ٝ���a�3nGw`e�ye��NoH�O`������;�v����rґ]��� ���� �w:!a�9߯���a� >wD��
(/,��fUA��.�{Bv<I�n����w�-!S;���ZI��e	�W�?�Gx�������&kE�q��[�c���6ڒ�r��C�b����?�g��������`_p���U��9z�VUZJ�[0��xh\�Qx@b�͑9�Ls�M�aa2�8^*����j��0_m�	�e�|��v�Ή���W8p�ӫo�$?���^Xf���7�� ��;U�E�k�.p�/����u����C���{˨�ҏ�AP�[|�u�������ז�5/��W�g��3B����9S�U�東z?sي>��ٙ�io�t#�Q�Y�ϑ�W9�9�P�����XzT�zܝf_ ��0p�'E�q���n�c�}�<<~�jW�42�����H<S��D�d-�����d��	�@�.�=V�xo�����=s��7��8�ֽ�ڛ~�ѽ3&W�-�_�#X��q-�9 �=������e/���SN��潊������G����ȚGd���0�ាy��FJ�}t2y@��R��/��-��� c�{;�%�^.��&@��I|r��2N�M�|���T�Y�'_K��_i=R���UX�i9�`�?6r��.��~|C���,	��7��_���r~�[����8C�HP50�j��}�7�m�Y�k�!^�%�����@�v�<7٬:p]�������%@��k��U��'�:�`�U��|��� �F�	"`�)8�ڜ!��I-'O��5At�a���o|#/o�N9t��~�&6��ȏ���u�s����7���N0K���G�(y{;�l=���Ҽ�9~�F��g�I.�i]���<�CX��
^p\�7}�ް(�8:5��շ���1}	�'�c�N����0�i�v8	��B@��F��qw^i;��9�;�~gk�g����߄��7���㬇=��9��`}�dq,���m����&���p`d��d��.#UU��qBC�R��`'��k8{3��u5�/$T|a*��du���e�{�󣎾�� �ot\�����u;s�{�q��;z�e,Jɷ��{ ��V���P��v�M�?ݳ�R+l�7ǃ��]����&Q���Ir���[��c3մ���ï���D�o7��5���_UFg ����fO4R��?K��7�{��!`�2��'�{+���C��?��A��J6�ҳ��Úgo�l���x�y$l����Z��;ڴ�Wȵ;o�����	=��gw\��c���Y�v9ؼ3���9�+��Ќ�J��������0�̞9h�M��MeG�3�~���`!n�fX<{�N�w����
��#�H�x�U���/��X�W�k�p�?�"��N��l�z�v��w�8>x��OCv 	��p��%��8@�iN�ũ��`��'��`�͊��,���a��z��;�����"j�թLpJk��WW9'�
!N9��4��v�v��쯜�̦/���9^U�m0�( ԃ�^�	?�@K�.��C܍Sr�WJ�b�{������r(^Be.�A^���,�6P�qh����k�S�X�ȫ�x��"fG}l���ca�Dgyy��J\I6�Ǫ	���r��A��m8E �6��Ô��?��Y@1�C�4 -�@<���[�t]'،"eo���k�1�����X�>�q� wx����}�o<;�>�>�L�R�Fy���z$4
�㼣��`&�:ڦl�p����D-��F��t�!o�%A�s <�d�s{��Sn�B�9�g��%_����w��,/�0\�Zx]��❻��z3z?����o�(޳�i��:f�q�l{ژۡg�;ۑ��c�^�	�l��ӄ>op�,GMy,t�R�1!2蚣T�2\�3H�+:m5�s��W��V�p�������}��������%oR���Y���>to=tƌ�����O�����^�)�Ơ@[vou/^[��Wg����w�I:�`µ15J�H�７�����5�N��\>�9����y�o�aם�lk����� ��ޖ������4[__�Ez��C]V�iC��@^�X���n�6 l
>g�|��ZS�,�i�%g�z�/&���>�$�uI�/������8�	"5P���0�]������-�sh�0F���MI��1�ƞ[���by��:.�ؗW�Z�ہ4N�HO���	�z (^|5�ӆ�lw�k>K�i/�(x���7�	�R�<��`4�n-r��UlN�k�3��-I�{t���9z�����N�Zz�{t�s�q���V=���w_����bay�hU�?w21*k'���V�dg�
YE!f^�� q��%�v��q���� 3^�5��<�B�՚�,\x��T`q�;��ek���/[z����mƿ�bH2 y��%3�^z9�/��=���x��'�5�� M�a�m���y?3bN����Go粧��ШG7�������!�^��@V��Y��o�"��Ý7<��<�?^��cFV�x9��m�5��)�ki��x��$I��Qv;v4����W�A/�t���#� =ǭ76��8L���N���A�@�u��{�9¹&���zu�޺����xs�~��78HѷC
7��fƱ�fmk����Ɂp�;7��'�(�۞�u߈�)X �#&���1W�y��7M����&�l��$Ny�]<��G���X������IZ�L���AYz����V��p�1xT���S��^䍌D����(�߲��z%�p��c�]v�t@�D	�/e�PC������	�����}k�iW��q�C�����w���)u<�b��W/�]�h�����A�6�V�
�ל�7A����M�O�O^߀Z�/��27+^��~$>���������A&/l|NF>�����L��E����ӰOM�ހ���?�l&��az�]��8��a݈B0%�_7�G���o�W��D�s�V�����ŗ�W#���n;ǶRaX��^��N�>E��kE��Z�g�Zj����^m����M��F��<Ϝ�Z�E�*�4� %�	�I|�`��E�;��u�dKP�~/)��<C��i�L�5�r��t�W��lVB��Qo�3�E:l���g�xY���y��ϥ��zV
;��J�O׌p�<1q?g��D���%,m?F������Ξ����C� �����$&�*i�d1�s�5y�l愃%S�h��זG�3��/�KQ�	<�.U��7�]5-���j�a���H=I�,%i���D};���t$�.���I:����[�\JD�~���j*���p�{/��9�O�;VO��`[�������>y��V��sp+�T�i+�    ��rN>߮m��M�ȳL_C��'�}x�	�d{yp�v����,�&���u
A491�F8��q\QG�3�m�O��{��\ۭc(� 2*�->R;�Q�`J����9sK|l%�;���*E��\�����SE1Iq�@�;�A��bo�F��O��$�"Q�d'�\����f^�DK�Le>�4�`��c�U�N�������MvD��Ɓ����R$��q���J������[�t	����o��}7������2�K �;{��=�|B�|���R
*Y/�y�P�}:�����������I�4��O����i7�O_�jz�*��/�d;Q�Ӄ%�B�t-�m�_nuy�O.`/���QN!=�1w�x�Wr��^L�f�M䒅�N~*O�/���o�|'�&�̠ѣ�i�,a���<I���n ��Ŏ =�E��-�zg7���9�':.l�XO�i��O�:��q�?��螚�~��#��
�����-DvE�$B�[p���M_�&I�����G�YS7�XL���
�+��(��(�O�eg)�[�1}�h�o���Bs�惔�ǩ��E"�j��ȡx�`gu���o:�&�a(�r��j�,�������J>a!|�����$��DI�W���FE4UĚ��=��!�2���P�^+�N�o�|����Tr)~�gO�C�溔ۚ�|H�_��u-F���C0�Xȗ����w6/�%�3g�a��x0�g��aY��	��mǒ�Th�;'+Q�X�$<��aJ]86J�\L����ս����&EN�8@�� �g~���z]FQFn9˹�v"9�o���+W�n��l����|��c�g�`ZZ~8�)���s/>:��	T��)�O5�'����P(��f��\/�PwCFK�,M�^����������SK�uH��y9��u���D��Cڢ�����1����Z��C��r����0>L%����_P��k� ��Îcx�P��skL��g��c9���I�L7B�����(��qVl�fJ 
�)�N�I63�̵���=����Q~+U�%_�ne�0o�T.2�T���B��⧜�K�I���&��
:WR�Ni{�=������|X��i���s&�F�h��[G�#7sr|K�ZD��\��q��'ǯ=M�N���SB~&�9'Bm�(+����f����R�1,�k�����Fګ �a$�~��ȁLO��!I>w �z���4�(=�v��i?�	-`>�~�N�&7�%��	}-�j�K!@�Þ\�X��ҕH&�;��m�uF=�fB������2��7�[���h����<a��[*��e� f�����^�E���%�M��7���E#��J��i�m��+e+�J]O�C�2�K��5l�8��J�:�p�G���*�e�3�(C��,�Q$�'0���F�i[nx��嶺������"�f+9F���K=��@��'9�0pR$n&��m��ޒF�
؛sN�$ֱK�A��� O7t���e�4��9�'/0j�T�e]�]6d���E� ��`��-�%I��B��k��SBgyg��A���5kҫ�qO�@��J��9~4 A��c�;��4��2��W*MH٫���u�6�y-Z	^��2�!�7���s�R��y/�O,}l��9��?�l����QC���={N�}�g )��X+�Ʃ#|;K�8������<�K)?&7Զ
e�N �FϺ�V���>�5"��ʌ�g"�v=���C�H�g����g.g���r�K�n����PmWR��j�-%a~%IrČ�즹H~O�o��H��>\-�=�E���y'P�T�pU�� ~������ef�z��>?N�LO�_��F9��;%r\�ma���	��k|A09�.O�%����� !:�8��9=	u�&��5�P>����x�GL��0���
�+I�繦Τ�q�BOA�I�G��B�R���~���4Fߔ�r�o�������T�d���9���`��oH3�$�'�����r�-W5� ��Ѹ�&)lU`1ǥ?ɡ�p+a�%��4�C�l����+N:��q��~��=E5�tU�:����-$'櫘�����8����KO=tLǄ`e~j2�{Q&��̉Ahص�<1]�<��$��������fpm�4�V�Q� �J{�#4ǈ�GW�^�Y�%��!���**Ӯ38�B�$��(�:��]jB%E5`��~^�����aSM�1/o�œ�Ms���2��0����A�*E"{���$ASAU˯>ٮ0��X��gK/t]�CI��vS�4���x���草�H��Re^?�I��R�D��]}|PEMj�y�v��ϸ�4޼L~��TM�o>��('����kzv�%�b'�S��?��=�h@/�ɼ:(�
!��ȴԺxP��<'�_�^��,y�#�=��]�Ҽ�t��{��<'��L>	�3K�|�3�_	�m$�fWr���D���i��J�r�+�\{�4{�5��fvyI1�V:R-c�d-'�$�Xp3�1�4QJL�`��;N��	�ǹ+��Z��l����Rj������rb�e�ZNx4�U)p�X�Q��TzjxG���;}�P��,��<���uM��)�zT�p��E�ȍduP�I%d~��� �ӕ����g<���"m�5)������|�kǽ\i��Na�ģSb
`�O�0�m�	�P��;t�x�$����9�FMJ�,��6=�qc�]�99�$̐�11�eɟ�eSP�)��\�嚴�ѦKO͜�r�*N�ړ>o>�I��<o^L㽮��r����Sci� _�rg�T�T�Sa&�����XmR�bs'��M�{W�Uz������ 5��9S���bHv|/�hf�f��'�޶!F\�=������(������<ؓ�t�#���6q������&ҧe~|֫�6� }�'����2fM�|�T�1O�痸���	\�нip�H��m;ul����cڅA^���^RY���o$�G��K�B2��\��rS&?<�ھ�3l��:�C�Q97-��OE�2��J�)�+i*����4!��fZm���ɜ���z(u����W�x3��$�|v�U,���n�M��Gu�ZY��?\t��N�d[~����M� *��[�9\��<G"F��"�c�Ԡ "GI���oB4#���x#+�<c�y�����������zaDr�e��f*���������7l�	�3��}RM�������N��y��G�枷=hqS��'�������Q����ƭ�L�Q�4�P��{G�J��6���p��;)Ҹr�2�]a�B��<&U�{k����}�����$8�t �SJ��ӑ��͵}�p�9����X��vm��-�H'��А��
�J����ŭ��&�Ց�K>�ed尥�z���g~�6�Mk��f���(��������H���)���qѓ��\q�gei;�Y�����ϸ5�T�3�PGhĸ�=�0��S�H��OZf�2��d-��7[pO!��	~򶥪�Q'�μ'%6�l	yԑ��� ����NP_�`ex�p�8?I�6�����D�tj��4���6�q-��)�ӾskK��ù��f��a��ʱ8�U?�Z��R�'���I���\[z�)\�D���;'�uK���
�}���7ӌ3Oη����C䨽o}�M�w����J�)+�)(p�r��
�"���d	�q��w]�e{ަ���I���M�� 3�}��Z�!>�&�:���5-K��ר�9�TQ���)&���U����O�M�P�
��'�����j�/�- Wȷ��1M�:��ȑ#�S��#�P���k������5�!޷<?��~�Q�f;�N��H��^�P������ڬ��cأ���a����~?�a^^�!��KyMO����L�ַڼ<��4%��6��u��kt%$B���SaC(W!����M;aO���<������E��3�%B�&Ty�gi���l׭���mu�hw�
��0��VS����Y��<���I_���Ӧ|�@/ҩK9u�    7#��<f����]Sˢ>ӳ ��=�y�7f�倦�<1���W. y��To6K��f���� a� �S��'���Q]�>�ׅ��C:��'4H$׋�L��$��G�2o3Ά1�L�f�{�i��	�\���;��������w���n�������l�x�D<�B���zS������9�����,̟C���gH�'��ej:x�)��ƸU�~L�9t0���~��H����KI���6�$^���4�i��*�������Z�䩓�X��	"�%|���I��BOivDR\�Ì�2�n*���j��C#�F�ڭ�Y���4�{�
r�Yj���j���y*��D�&��v��ҚBB3�R���q����X���Qy��\/�E�,,��<=�)�r�tLJ*{��׏Ś`4����t7X��R��i]Қ��L#�s��<흦<`�K��bm��,��,h�쾮h~?���46$T�}�I�l�O�J�E>ҋ����&b�d�R�H�'���ếr��'��2{�=�D�Փ� O����))D�ț�˹�[Y�]jbT�E�DM���c4���\�q �ݵl�&h��&O)�J�bOew��H�{#p�����WL!؟>�[�:)������������!V	�]�R�4~��OZ���Ɔ6/:���R�Lݔ�B"8I0]���pn��t�WY3��:9�4_֗�n��}�
�oZ���h���r(�;�e��(�Az��Z�R�D�<���;�9��Dm���ߖ��E��oHU���|�+E�ɂ%��Z�b����+�DZ��i��l�h�=+����.�	�R�U�g"Y�փq3���,(Qi���5,�-���䩴�ϳ�/!��Tr�;�|pS���	G�7�qTC�>�E�� �>=�����$n��I8ո����+��kB@UM.L�&4�93�	03�kNEWܤb���}��Y'{N���K}s��~� $>�
Ml�3���E��هg�k����|�� �Md��HQB�J+E��c�krij����"�hݫ8�l�e�
�ӳ��?�WYJ�� �R�E�_��C�x����6X��x��� ���}j���7�����S�tZ[�)�vVEmO�8H��(�_��D/DBD�?��$@��|����Vt^r��+A�3����>Y5�H��L
���GTᒵ�ڙ���=N4��7�s\Ⱦ���e���ٴ|��k�|V'�jE����z�q�����3e*zޅI�byO&���e��x�|��Y��2�̗J%6ۑw�^����ag���gA���H�Y�ʈ�B�0�b��Υ�����*۵�����Y����������K;K>�)��	x�����$�}hٹP�ݿ�ҽF���lOּ��oC��g��T|$���O�X�)5�T��a���V}sg�tqI�Q�Kk<�Fw3�LT-D�X�;7���8~�.);S7X^dp_�ަ�/ʀy��F}r����4����O��J��m|��r�m�gR-�Ք_D�LD���hP��o�����o�8OE?�NO�K�>@�{���9�����)��ת�i�3*�u�O{� ˼9j
�
t�/��D��g@R.N��֩v^0��B�쇱����6�Ky%%'�LЉa��v�]��
�7> �:�Ɲ+p�d��h"��� \XM[%�}O�K���W�̖K��8ھ��I�L�ܬ~b�W���~�HarF�z�虜f%�6��e��N���ؽl�t
w����d�����:�ɒ�8���
��n��H,,��i��w�wJ b�&�3"����]���i=��-��"�Qº���e�e���Q7K8�d%c��e�9A"}sj��ÔzsrU(�??��\w`�����Gb��� C������[���I|$�lfr���ܨA���<�Q�I״v�O%`���sZ�f����w���o�M���j�����r��ħ�k�v
'ȑ�|Ӽ�d�Ԅ<�F4��O�$���i�F�@#_��	j��^j��c}���v���+�îJK� ��]b2}��Ц���I�{�-���@1�h9[��`OZ3ŰJ�^��\��W����֛�n*)������x;���X���]y��g�N�X��$=2�Pm;�Śr�I�>���Z����6>�`-!</b��^�6��9a�9^Z�k�$�����/��?���I��<�����}Nu68i$N�����Hs��?]k�s��\���c�A�2�8�Й������'�K ��}]�K�2�w5�?O�)�T߿K�r��`�v
���/]J>M����U���V��}-�r�V��77N�O��S"�SZ���K#�ϡܤ��� ��&�|G���g[�S�d����=03u"�O��c+0���Mq��Er�o���A�a͔Q���VC�U趟�&Tg��������ު�d�����zk�3n����xMW)f�Hg�+�k*i���;�4�xo+�݇m��⽽�-� mlUv
F���7��DLbjW�q��V-PI~�y��G=!Q��y����P"]"ƕ�[�4U�.���r\e�e޲��� ���%��b񊶫��4�ɹ��k,��n��<�;L)�L1n�O�+�ы���~4F�4zާ�3-H>�L���*Wͽ�'?S��\�ZP�᜴��i�Q��B�9�%����M�����Hm��R	�m^��*6�-��ۭ�҆���<Ք�5=<��k�b�jn�$��肦tzJ�T���Mq�S;\m�
��*�>hs�0L]���nq_�=ή4��v�+y�v�_Diߦ������z��
8��mHy��0M��a�h4�ꋩGo5+��|c���dyL�j��ތ{�"������@�c,3 �H(m�m����|N�߷Q��Kv�F�5%���R�8�N�.��_C3���K{�n�G���ߋhQ
�YU����b�J� B�7f&)�G�㍻�0	���/�~�% �u"�9|M�ߚ�w���:�k��V�5Z,)��d���h��\��:;�Q"&�ˉ�`ʀc��o���"gCzs΅�I�A�UK�i�I��c�)'�~,��\X��A�aJ�ʅ�����f��7�.�?2Ė�g��?��w��2:I�zX�痽��<s�66`WI��Y����� �M�_��wC��
͋�RvכZ��U����\����(d�U�'�A�߰j/��K�ޓ|jT�^i�T�R�5FʸS��D6i4.C�䫞�mMU��'���"���;`��3'y:OZ��MɈe�h.b81G'�i�-o����4E�� �;���s�EHI����Ov��M�����j��Af�2�m"�R�8,(�B@�G$�\�*��l��NE�jr��u���'���t@���)�/��X�����dK���&��Q�R+�1��DfZ:���Lu��M+�;U����#{3 +t�ϼR��I�z���Q'�4��ME�\�R��u��kj�8�x)�����N�1������@�K��@\�m��϶k�XH�j���+X����&����f��\���0�=œ�e�6��'�����C`�nhEL,t��Q��D� �F{�r@I^�y6
)h��
w���yBhN|�fz�z�l��./��D�,kk��~F�i����~���۩�&��|\_�Jꥤ�n�Y��$��N�&+���֦���V.�(oV牺�Xz�k�(���n�����7�P�|����-��I3Q.��5�Qr��=�� `�4P���&�5�P�����%��)�Rk�[ұ2O7w���[��E.P��l�Nz��%z���=/Sy�j
�tn�l��!3ѝ��Z���zk��@��y�nkh�~�($�(( 0� `βL�����{	��Fx	�D����<7�/���UjO�D���Jw�lSDK�y�k�`tra�p$_���rnS*���:���>0wÐ>��W �I*M�Է�rS����Jb,loN��.�_�i�i�x�~�c�6�υMeUT���'��!Ŵ���i�o�o{��R*�֋7�D�27#@��%��G��;y��G&��    ��.!�>%��@m-Qၶ�ٹ���-/������S�9�)�m�	��>�oX��V'qlt)Z���ٻ�,,���&�ɱ��)����˞�x�o/�꜈eMȜ���,kE*�vX�q���SL 
g�X�@�J#�c;�4	L�*S��WsW��$�w<k觋��V#y-�Co.$���j��HXL{>K�)� ����/�gc/n���ee��w(�H9_�����",ۨ"�"�V�n����.q���ze7
�\�m=b��Naw^z�����g
��j��7��R�=17s� V���	���-�}哚h�J�����&/�,�^9��s3S35��h�o�����a�Q�0S5�; ��d��"I�;ǈ��٦�|�ҲkM*J��v��1�Ml�[Ƞ.����-e-�$������nTn �����wgA��cW&����+N_2�����<5�.���Q���2�P��(͠���]��q������3?e�q�����c����$m��5U����� 3��w�@����H*�����#i��h�䡔�y|����%u�Ҧ=����䂑��\��h0����|�4e.�|N+�'��r� 6���<Q�?�d��1��Ǘ�G(��i����tY�j'��7��;i'�PB�@u�u�:���n�/��������>�6Ȁ��~��9i!�tR/�2�u�E� ���AƷy�%S����(S�iZ���l*�T��"/�s�����am���ǟ~�����n�Uv��[���)��l�F�Uگ$�l�`X��!����-V�Qq�A�a�l�z2���ٛ�`/��m�"�B$5!�L�-U�V<�����M$��~�{�JaH15Vk�{*a+*�et�^kԔ�$%i&a;�)� �m*m�]r�����׎��	;<N-@y;�/�r|1M��Λ͈#ךK�S� ��ێ�bऔnD�O9l�{@��/;L�$��8�`Oi~��P(�D��-��ܛ�Z� �(4>��R�
#
.�l�R�\��x>F0�m�I�y�p͘n���+�c�T���S8���C|~��B��gD�Lzl��&�D=2��s/g�:#O+�2����-��&pռ��5)B��3cg:i�[q��;�]�������4O�Bw�����W��y#&��k}�N���%Hvz��Sa$�Ir/�vf7+��mL����\����S�դV��Zږ�$�K��77?Q��]H_�����R�)� �8�0�/��|���|��Jϴ��/T��3 �v�����oK
��pt%���B�^J%�%M�I�6y��$��"j�K�Ƿ|��"9��`�	��X�|â4��ɝ>�({��$�^/��+Epj��Z(%u�P:$��D?�2�`ҙC�<[!�84@=sI9T����juP�g��n��,z����`�4s>�Bц�l/S�s�9,l���1$���oQ��{�]�������
A�i����)�����9p̽.�T�vn	�'=����ZR��O9Q��s����S�)�P����_��0�.pc;6�X����>D]�����,���L�iƵX���wn�{���H��|���#]Mz�I��J8��_d⽊�}����aQ�AW�"�� -vZ��#�1�q���ꌥi��{q>r����'�3+�$��|�3��X�)���Fi�&���y����TV ��v%�+{K��xNan�,o�`�)&]Yw��[X�qHw�s	�?Vw�G��iq�c.d�n�QҾ�g���w��*W�i�,��¬�qaX����U�؍��`����*tp�4�R��D�ύ���B�Nl��F\	�/���)�VݾO��h�'A��,I�<���i��R"�I�,�A�5�Ћ�[sO�3����̀�&����y���j�a��46�rXB�����o�y�h���"4�x�b#����W��>P�6�����r�s���ME�P��c����|�R�(�G�+�u�/C���ki�۞0^>b"*E��~4�y꼅�?�o��4�gb��osV����=N�!r��A��������F~��)Yyϣ^r �$9��7Ѽ$��l��!���� ����P�URMG"[ߋXj�G��LWv��$�$l�z���o�ͺ������٥V����Ri�WV?�箱��}� /9�>#��R�%��1Sj�	���<�\��8:-֭`��g1�O�6~KC��7�KI���X~�%�/v8�';R�Δ�W����~�`��AP�Pt;!ޭ����td�ю�ZK%�� ��'�&�����[���^�	�<�XP{�		�򸑶�/�Oth���_�䩶o.�rι;2�Z�$�� 1����.g;�i�!}쎚b�(N����Q���l;��f{�?>UOtJS�e�M�����d�g�$��nl��I�:L�Ӊ��w7���gx�π������^��5�=!ܰݵ�N8�h��9�C/����x���� r.'�4��W8:������`����p&)�I`EG�r�1��1@�����<ty����(W�0#������h�件�)�0���TY�_S����I��r��ϴ��d9k���@��gc�|X:Q+1�^�&�;��c�f���s�0s*L�P��v B�ZqSH��OR���o -�P���L@5j�F��[���u�0�'�Ӕ�?���E)$��Mݪ;N�?�F -����\&�|�:��e���&Yj��:sܫ:o@���SVh꼔))&���C��_����K(^��s4�%}#�D��q옏��9L���!�wR(��ȼ4�U-�L7�q�m�-Bbo�]R2�_uo<�T(�y
�aȷAEtkej�wO�s��yd��������(y�3�L�os��mvH��*7棼�}*Y��q`��.���{U��\���͛?�N�W[��6u~�or<;�XY���6Z����>�ҩS����)64�'_2�ugkJ��T��ө,�t�]Fv�Fp�8�Q�ax<�=�H��p�,����S�v'##��IO9�T�o�4p���HfiR�p��L�L���p����<�2��2w:� ~����5j�i�	�v����9���Bq��WW<��$�&p'�T6��Cr-����'Ib��NY֯�W>D��vE�%x2V�I��-t
���Ki�Ϗ0��vY���lgs�_9Y��"9����]آR��;�C���}�Ӡ>ɜB��=U>נȕAۦ�"�HJj�E�ހ�.�j����؆���|��N�ұ�USٞv�	-�Q����H�2�j�:�lq`,Cx$$�>ar"r�ʧ6�`Ff�+N3�G[s$��m�3�P���0w�Rf�l�y�9i��^�V��$�����ӛ����B�O�?��,�ԙH�60χB��:X��ȭ�MOJ)��rgʅ����*c>�`�W.�lpqV�cH�=I��+�I�wv�&�$�]+%	��֍��E���k�����6�׆x����@�� c�h��$!�� o�Nr	�j�<�r>R�S��C���:���Ne�m̘R��ƍ���f������a&^~̏M�$}��ؤ�޴�J�6��k)GX�7�ӂ$�5HR���홊�d���Q"�k��$�D�}W�ys�Չ1u����l��P۞NĢr��c���~�ҥ��9W0��Fo�_�6z�9���c�iW뜕��X�DwBt��Ñ�(#lbyo^ݸ~e�dh�(q�<V7k��� 4WPJ�ӟ�x ����R'�Y����}�zI���\���6���:	_�x-�`ɀ� >��:�A����)i<e�
]�O���Hc]�����(b�l���G�C $/�>x��$9����O��LdQAF����,��	�Ǌ�D>��H�ʔ����Q,����XrRY�@ۓ�d:��ʼĢ�Q�;M6�YC\׽����@Iv��ar)�-OtSR@!g|'�i�AU:�;g�^��/x�i���OW�i���S�vĠ�I56�?�$ )��$������t�Ι#�])HvO�>�OQޟ\1W�N&�x�3�-���3	    �tG�?σ��	h>�į�1��Ӛ�S�H^I+�Ȳ�I�Փ�Eu ב��?���Q��jmD�p�=�d�-9Pێ����&�/�:���X~��a��s�z�BKl�tI�:X�Ѻ|o�<UzE��&��z�a	��D��@�;0DRȣ�=�-�?JڀԄ�5�E��L��K9�Q�MĬ�.���h��D',�=�_`ay����P�OK�J��7�Z�G����ȝ�8�S�bcX��g��
<�*��FU�Щ��r��]��5�\k;Tr2q�U�ͮ["�d顰<�4�)s�Gz_C���K������}��}����4��S?��a��9ÛS�JL�o�=M��¯���Ry�E\[�Ws�/��Ɂ��K�j7���=� �<N7N\��]>
 c{�����8�d�g�U��f�$����+C�s��)-%@�|d�1�{��� Ύ� ��
!�|T��g�oG�g�*;Q�H�L����*�,t>{ЅL�
؍H�v���К.�'�����n�~�~zRI�֟�z	W@��$���K�܈��H+��?73�+T��#yBU14��tQ�\���J*g���O{�"	�`!��0��$N+�?�U�,�,: ���6�'�Q(	���3=�Cx�=a>/�rp�aq�<ۘQ!���+-�{+�9�Ϻ�;�7�S�I�%s���9�x�۲	�M����H�Gց��A"i�bX�1�:�}%����0��4�J�dp)G�ݪ�e��|s�s�>	1��O��
w딍��6O���&&j�v���)o�P�J%M�<SO� ت��N)in�V��F�CӺ<�fX�Uw���_&jO�qo��\p�l��9L�Ĕ��ļnw�z@�)a
;���6�B�i?�SN4���D��	wн�?�>�k~W�:��z?�����<2擹���K�v�?9��h���(��A�uEz�i���
�_��(� 7��D;�u���7��[��,�|��TdI�!I�O�
s��V��g�92O�8��z7_��('���ȣ�TG�ؿ�7���O����I$���N��D
;䑝�^e �c���yC��ͱ�Ѵ3E����NO�Q蘗(�=�hJ4K�z�5�u>ZW+$��	n�*�u]R��Ʀ�������&CP���| S�"�k�S�l�y��{.��$u)����o�/٪ �w�B�~~����XH���y��W|cߏ��:-G�)8X�����Q�"/ߣ��!�V�"�.+���Ϳo�0J{�+:sD'�+I\��N����V�x��Ϙ����R7�X>�����X���!��p��v��{��0�KE���ʍ�#�v�+(
�I�X�5ڿ�+���x�%��%KA"��c�/�f�2GEC��mY��ڒi�`���r6���z̶^���.LqS��.Z2�F@z�^b|�.z�e&%�4�?FBZ��� ��w<�|k���LY3�	�仸� ZS���f��ʿ�@lg��4Y'�R����n7�5}�Mݽʍn���HS�$~p-3#I��6�e�� fbvQ��m�=�^���� 4$�+΃��eRk�5׬b7OyK�5�+����KY�x�#ف�����<s�!ߓl��*�OI��}��Ȫ�}�݈�x-S	3�3K�ܸ4����������=-$!�������ԯ����d�:�t����e�4DK���{Qp���w��L��g@=<oH�%�*�H�&����r�B�%U?��y$H�s'�bo�"BCPhۋ�B���&
?�~f�,,o�?Uv>JRgB7��4��?95�i@c��=:�:�<�T��X��ͤ��5�H�J���)Wa��*kEd����r�YW�+h�0,͟��%)1�V0�T�%�t{�Ih*�c���:i��L�UZ�l�I�*N�Fl�,�R@غ�ix�����J��o�A�OX 0k�JZ�SgΥc��;��R@̣ފ�F��N�ϰ�4���Q�!����&���ʶ��9_�:���7��s���\.E��q+Ap3��]��ج�����ty�]�t��63���d���r)�ROr��/�
N�)D�&�LB��I�;,�\�pLv3�l7[�Ui��W(�)�gc:�pMp��y�7���� �&�'���"���C�L���$c�ՕC_Jc�|?�Px�i$�����7�0��K]��6>X�\�@udC��i팃T�9�x�\ �KB�Ȥp��7�����H����>KѠ�mt�o����4�{5j9R)�;O�NC�h�X8V�C.nf]��꜒�s��{à`n@�4�����hk�Yh���bj>���������}�> ��>1��z�����Z@q�{���R���QZZqqK��1�y�)BK�Z�����w~B���G�fڪF��a.��ْ��~�nߕ=%Xw���S�*�C�h�J���E��2w#h���/|���36�3%�s}^��&aLH�C���;�%5��7�m/2��_+����r3J�(�W^]��^6g	Lb9^m.�$��9'6R/w�0�h��僔
9�I�b�T�f�]���Q�ae��S�%�zҌ]
c٭J�9gcBt�K��u(�Q�>H!�p�)ny	S�\6�)�鮧b-)���3C�u~Tj�61��=��N(�	�D���!ؔc���z�,l��\��U�AEɇ����M�5� ��sr/���o�j�g��El��9.��S�$nTxB.��&����r�t�r�Wa%�
��|�@�Qm�X==(]�u����$SR]�T���V��=PM_n���zm��W�t%Ƀ*��tO�^�ᥨ�T�$��0��L4�y[^�9�eWuQ1,�O��#6�q7= �j÷GL�v�r�I�C�Ȉ����+I<��"Y�6�˟��N�qP2�ǲ��.%Q[�#�����V���uÅ�D�'��'ݬ�ú@l�XKn��l�1�):E~`�sOʻ�K��#*�-�;ՒD�s�G\ 2�:p���'d�Ջ��P�(��
��^�ǼA�w�h>��*�ty�i��W��%���C�r좌��ߧb]~X�%�� GI�o�W�ɆwCH�l��ӻ&Ne+��ֲ�$E䏒-�ܷA�8M�\����Hǋa�����oi/�������f�T��⃤�d�����m�p�;�����Ow� 3m,o�,+#m���֧[���Rp?�F�^`��X�^���-�w+�6�����=�}�=t|G���I�\�$��y��V �2�1�
�6��㒷�R'��\}z�d��{UK[M�/�̜h���Q�H@. ��t�Ն>�@y4��5���p�i�ވuk^�?����eLV/iLGM��Wم]��WF��,%���@^t���忮c��oL�A�0�;{����?~�lB�	0���=Gu�v0��X�~XK����L��e	 ��Q�i^���O��A�З`P�}���UoNs��=%8<
�)�����Ret>�gJ���L����6@Ԭl~L�Ƀ����~.ҿ8�+i$6��JȺc��Dx�t��>W�������t@���mBu�f���mI��˭z�0��d�ix�_'�Fa`�0V�r+�{��`����c*6�sb�u߼���DW�v�c�S�-��S�m|�%�è�� g#|zA��S��U���6PC1O�3�O�R��#C�˃+_��SqW�Ӳ���m�-ڤ��lWy,峔���,w;õ��	��ծ�~�V"�׻�6���,����yP};�8��|!MS�L��~�h�Uq�/�����ox��:.D���dJTf��5���@�6��h�5�$�of�@�ek��A� ��W�����J6̝ڂ~�Y��4���ڇ#Qa�ҿ�.�����k����K��)+��<��wS�~��E�a-�b`'۱X�
�)1PǍ��d�S�#���_��U�+u��9�/��>��6���=y��XR����҄��{1�}�䂜�8���Sl�;��<��%D�����Ԯ*J��&���0(�)�`�k9؍�ȼ�>�z�P��M��k�;���q�L�~���ґ��^p�m����U8�%[�dO��%�
`4���O��.l���ފ��� �  �4���֡{�7�;ebs_۲���w�I������h,����K�J�ۡ��9Q��2g�����8#r����k����I���|�-��n-\׹ۍ�">T�������2?��u1�D�\�~�n�����I�IIS���
ZDڶ��o.?Rd����x@઎����_F/�a�2�Km���p����`~��))�K.�G��5��x��Ay^�z�Xݦ���`8p2�*nI���]>��\?�&���x����6�#O����I���ك>`YM����f�B��w�O��+�Ŷܾ�E��~�N�fJ-[�V�ǅa��Rڱa�t��8����-p�p�j362���.T4K�i$���?�K��(W�Y�	% ���0%��8�ơ��p���,��� ���M��o<�G�Û�����0^�s�2V}�311�% %2��t�{��Oc�>�d"��B��'��t�Q\}�R�_�"!6������r��r|(s^�|�ߗh���	n=/��Q@����Y
������N}�d�RZ'������T ~w���zL�Zk�e���!�`��N�B�Ooy{��ɭO"Q�oy�\����MJ��r���ԛ�M�u��X)�܂��x�*�b��	�Mы��3�{F��||�J�q)u�rlӺ�F���;�Z��?���A�G}0n���d9��������y���/�����ߏ�������������������i���ݟ����9t�      ?      x������ � �      B      x������ � �      C      x������ � �     