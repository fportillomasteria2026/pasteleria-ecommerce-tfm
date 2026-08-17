-- ============================================
-- BELIETA - Base de Datos en Supabase
-- Ejecutar en: SQL Editor de Supabase
-- ============================================

-- Tabla de Materia Prima
CREATE TABLE IF NOT EXISTS materia_prima (
    id BIGSERIAL PRIMARY KEY,
    
    -- 1. Identificacion y Clasificacion
    codigo_sku VARCHAR(50),
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(50) NOT NULL DEFAULT 'MATERIA_PRIMA',
    
    -- 2. Inventario y Control de Stock
    unidad_medida VARCHAR(20) NOT NULL DEFAULT 'KG',
    stock_actual DOUBLE PRECISION NOT NULL DEFAULT 0,
    stock_minimo DOUBLE PRECISION NOT NULL DEFAULT 0,
    stock_maximo DOUBLE PRECISION NOT NULL DEFAULT 0,
    
    -- 3. Costes y Proveedores
    coste_unitario DOUBLE PRECISION NOT NULL DEFAULT 0,
    impuesto_porcentaje DOUBLE PRECISION NOT NULL DEFAULT 4.0,
    proveedor VARCHAR(100),
    merma_estimada_pct DOUBLE PRECISION NOT NULL DEFAULT 0,
    
    -- 4. Trazabilidad Sanitaria
    alergenos VARCHAR(255),
    condiciones_almacenaje VARCHAR(50) DEFAULT 'Ambiente',
    dias_caducidad_media INTEGER DEFAULT 0,
    
    -- 5. Campos de Auditoria
    activo BOOLEAN NOT NULL DEFAULT true,
    fecha_creacion TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    fecha_actualizacion TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Indices para busquedas rapidas
CREATE INDEX IF NOT EXISTS idx_materia_prima_categoria ON materia_prima(categoria);
CREATE INDEX IF NOT EXISTS idx_materia_prima_activo ON materia_prima(activo);
CREATE INDEX IF NOT EXISTS idx_materia_prima_nombre ON materia_prima(nombre);

-- ============================================
-- DATOS DE EJEMPLO (3 materias primas)
-- ============================================

INSERT INTO materia_prima (codigo_sku, nombre, descripcion, categoria, unidad_medida, stock_actual, stock_minimo, stock_maximo, coste_unitario, impuesto_porcentaje, proveedor, merma_estimada_pct, alergenos, condiciones_almacenaje, dias_caducidad_media)
VALUES 
    ('HAR-FUE-01', 'Harina de Fuerza', 'Harina de fuerza gran formato W300, ideal para bolleria y panaderia. Marca preferida: salopesada.', 'MATERIA_PRIMA', 'KG', 25.0, 10.0, 50.0, 1.20, 4.0, 'Distribuciones Malaga SL', 2.0, 'Gluten', 'Ambiente', 180),
    ('LAC-MAN-01', 'Mantequilla 82% M.G.', 'Mantequilla de cobertura 82% materia grasa, sin sal. Formato 5kg. Ideal para massas y cremas.', 'MATERIA_PRIMA', 'KG', 8.0, 5.0, 20.0, 8.50, 4.0, 'Lacteos Costa del Sol', 1.0, 'Lacteos', 'Refrigeracion', 45),
    ('DEC-CHO-01', 'Chocolate Negro 70% cacao', 'Chocolate negro couverture 70% cacao, ideal para templado y coberturas. Marca: Valor Cacao.', 'MATERIA_PRIMA', 'KG', 5.0, 3.0, 15.0, 12.00, 4.0, 'Chocolates Valor', 3.0, 'Lacteos, Soja', 'Ambiente', 365);

-- ============================================
-- TABLA DE RECETAS (existente)
-- ============================================

-- Si no existe, crear tabla de recetas
CREATE TABLE IF NOT EXISTS recipe (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    instructions VARCHAR(2000)
);

-- ============================================
-- TABLA DE INGREDIENTES (legacy - opcional)
-- ============================================

-- Mantener compatibilidad con el codigo existente
CREATE TABLE IF NOT EXISTS ingredient (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    stock_quantity DOUBLE PRECISION NOT NULL DEFAULT 0,
    unit VARCHAR(255) NOT NULL DEFAULT 'KG',
    category VARCHAR(255) NOT NULL DEFAULT 'MATERIA_PRIMA'
);

-- ============================================
-- VERIFICAR
-- ============================================

-- Mostrar las tablas creadas
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public' 
ORDER BY table_name;

-- Mostrar los registros insertados
SELECT * FROM materia_prima;
