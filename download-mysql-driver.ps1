# Script para descargar el driver de MySQL
Write-Host "Descargando driver de MySQL..." -ForegroundColor Green

# Crear directorio lib si no existe
$libDir = "lib"
if (-not (Test-Path $libDir)) {
    New-Item -ItemType Directory -Path $libDir | Out-Null
    Write-Host "Directorio 'lib' creado." -ForegroundColor Yellow
}

# URL del driver MySQL Connector/J 8.2.0
$driverUrl = "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.2.0/mysql-connector-j-8.2.0.jar"
$driverPath = Join-Path $libDir "mysql-connector-j-8.2.0.jar"

try {
    Write-Host "Descargando desde: $driverUrl" -ForegroundColor Cyan
    Invoke-WebRequest -Uri $driverUrl -OutFile $driverPath -UseBasicParsing
    Write-Host "Driver descargado exitosamente en: $driverPath" -ForegroundColor Green
    Write-Host "`nIMPORTANTE: Asegurate de agregar este JAR al classpath de tu proyecto en el IDE." -ForegroundColor Yellow
} catch {
    Write-Host "Error al descargar el driver: $_" -ForegroundColor Red
    Write-Host "`nPuedes descargarlo manualmente desde:" -ForegroundColor Yellow
    Write-Host $driverUrl -ForegroundColor Cyan
}

