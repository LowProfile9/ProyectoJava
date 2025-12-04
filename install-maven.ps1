# Script para instalar Maven en Windows
# Requiere permisos de administrador

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  INSTALADOR DE APACHE MAVEN" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar si ya está instalado
$mavenInstalled = Get-Command mvn -ErrorAction SilentlyContinue
if ($mavenInstalled) {
    Write-Host "Maven ya está instalado:" -ForegroundColor Yellow
    mvn --version
    Write-Host ""
    Write-Host "¿Deseas reinstalarlo? (S/N): " -NoNewline -ForegroundColor Yellow
    $response = Read-Host
    if ($response -ne "S" -and $response -ne "s") {
        Write-Host "Instalación cancelada." -ForegroundColor Red
        exit
    }
}

# Versión de Maven a instalar
$mavenVersion = "3.9.9"
$mavenUrl = "https://archive.apache.org/dist/maven/maven-3/$mavenVersion/binaries/apache-maven-$mavenVersion-bin.zip"
$mavenZip = "$env:TEMP\apache-maven-$mavenVersion-bin.zip"
$mavenDir = "C:\Program Files\Apache\maven"
$mavenHome = "$mavenDir\apache-maven-$mavenVersion"

Write-Host "Versión a instalar: Maven $mavenVersion" -ForegroundColor Green
Write-Host "Directorio de instalación: $mavenDir" -ForegroundColor Green
Write-Host ""

# Verificar permisos de administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "ADVERTENCIA: Este script requiere permisos de administrador." -ForegroundColor Yellow
    Write-Host "Instalando en directorio de usuario..." -ForegroundColor Yellow
    $mavenDir = "$env:USERPROFILE\Apache\maven"
    $mavenHome = "$mavenDir\apache-maven-$mavenVersion"
    Write-Host "Nuevo directorio: $mavenDir" -ForegroundColor Yellow
}

# Crear directorio si no existe
if (-not (Test-Path $mavenDir)) {
    Write-Host "Creando directorio: $mavenDir" -ForegroundColor Cyan
    New-Item -ItemType Directory -Path $mavenDir -Force | Out-Null
}

# Descargar Maven
Write-Host "Descargando Maven desde: $mavenUrl" -ForegroundColor Cyan
try {
    Invoke-WebRequest -Uri $mavenUrl -OutFile $mavenZip -UseBasicParsing
    Write-Host "Descarga completada." -ForegroundColor Green
} catch {
    Write-Host "Error al descargar Maven: $_" -ForegroundColor Red
    exit 1
}

# Extraer Maven
Write-Host "Extrayendo Maven..." -ForegroundColor Cyan
try {
    Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force
    Write-Host "Extracción completada." -ForegroundColor Green
} catch {
    Write-Host "Error al extraer Maven: $_" -ForegroundColor Red
    exit 1
}

# Verificar que se extrajo correctamente
if (-not (Test-Path $mavenHome)) {
    Write-Host "Error: No se encontró el directorio de Maven después de la extracción." -ForegroundColor Red
    exit 1
}

# Configurar variables de entorno
Write-Host "Configurando variables de entorno..." -ForegroundColor Cyan

# Configurar MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "User")
$env:MAVEN_HOME = $mavenHome

# Agregar Maven al PATH
$mavenBin = "$mavenHome\bin"
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
if ($currentPath -notlike "*$mavenBin*") {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$mavenBin", "User")
    $env:Path = "$env:Path;$mavenBin"
    Write-Host "Maven agregado al PATH." -ForegroundColor Green
} else {
    Write-Host "Maven ya está en el PATH." -ForegroundColor Yellow
}

# Limpiar archivo temporal
Remove-Item $mavenZip -Force -ErrorAction SilentlyContinue

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  MAVEN INSTALADO EXITOSAMENTE" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "IMPORTANTE:" -ForegroundColor Yellow
Write-Host "1. Cierra y vuelve a abrir esta terminal para que los cambios surtan efecto." -ForegroundColor Yellow
Write-Host "2. O ejecuta: refreshenv (si tienes Chocolatey)" -ForegroundColor Yellow
Write-Host "3. Verifica la instalación con: mvn --version" -ForegroundColor Yellow
Write-Host ""
Write-Host "Ubicación: $mavenHome" -ForegroundColor Cyan
Write-Host ""

# Intentar verificar la instalación en esta sesión
Write-Host "Verificando instalación..." -ForegroundColor Cyan
$env:Path = "$env:Path;$mavenBin"
Start-Sleep -Seconds 2
$mvnCheck = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvnCheck) {
    Write-Host "Maven está disponible en esta sesión:" -ForegroundColor Green
    mvn --version
} else {
    Write-Host "Maven se instaló correctamente, pero necesitas reiniciar la terminal." -ForegroundColor Yellow
}

