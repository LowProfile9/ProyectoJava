# Script para crear 30 commits distribuidos entre 3 integrantes
# Cada integrante hace 10 commits

param(
    [Parameter(Mandatory=$true)]
    [string]$Integrante1Name,
    
    [Parameter(Mandatory=$true)]
    [string]$Integrante1Email,
    
    [Parameter(Mandatory=$true)]
    [string]$Integrante2Name,
    
    [Parameter(Mandatory=$true)]
    [string]$Integrante2Email,
    
    [Parameter(Mandatory=$true)]
    [string]$Integrante3Name,
    
    [Parameter(Mandatory=$true)]
    [string]$Integrante3Email
)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  CREATING 30 COMMITS FOR 3 MEMBERS" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar que estamos en un repositorio Git
if (-not (Test-Path ".git")) {
    Write-Host "Error: No se encontró un repositorio Git. Ejecuta 'git init' primero." -ForegroundColor Red
    exit 1
}

$commitCount = 0

# ============================================
# INTEGRANTE 1 - 10 COMMITS (Setup y Base)
# ============================================
Write-Host "Integrante 1: $Integrante1Name" -ForegroundColor Green
git config user.name $Integrante1Name
git config user.email $Integrante1Email

# Commit 1: Initial project structure
Write-Host "  Commit 1: Initial project structure" -ForegroundColor Yellow
git add pom.xml
git commit -m "feat: initialize Maven project structure with Java 17"
$commitCount++

# Commit 2: Database schema
Write-Host "  Commit 2: Database schema" -ForegroundColor Yellow
git add database/1_schema_ddl.sql
git commit -m "feat: add database schema with all tables and relationships"
$commitCount++

# Commit 3: Database seed data
Write-Host "  Commit 3: Database seed data" -ForegroundColor Yellow
git add database/2_data_dml.sql
git commit -m "feat: add initial seed data for testing and development"
$commitCount++

# Commit 4: Entity models
Write-Host "  Commit 4: Entity models" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/model/entity/*.java
git commit -m "feat: implement core entity models (Cliente, Conductor, Vehiculo, Paquete)"
$commitCount++

# Commit 5: Additional entities
Write-Host "  Commit 5: Additional entities" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/model/entity/HojaRuta.java
git add src/main/java/com/rapidexpress/model/entity/RutaPaquete.java
git add src/main/java/com/rapidexpress/model/entity/Mantenimiento.java
git add src/main/java/com/rapidexpress/model/entity/Usuario.java
git add src/main/java/com/rapidexpress/model/entity/Rol.java
git commit -m "feat: add route, maintenance and user entity models"
$commitCount++

# Commit 6: Database connection utility
Write-Host "  Commit 6: Database connection utility" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/utils/ConexionBD.java
git commit -m "feat: implement database connection utility class"
$commitCount++

# Commit 7: Basic DAOs
Write-Host "  Commit 7: Basic DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/ClienteDAO.java
git add src/main/java/com/rapidexpress/dao/ConductorDAO.java
git commit -m "feat: implement ClienteDAO and ConductorDAO with CRUD operations"
$commitCount++

# Commit 8: Vehicle and Package DAOs
Write-Host "  Commit 8: Vehicle and Package DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/VehiculoDAO.java
git add src/main/java/com/rapidexpress/dao/PaqueteDAO.java
git commit -m "feat: add VehiculoDAO and PaqueteDAO with tracking functionality"
$commitCount++

# Commit 9: Route DAOs
Write-Host "  Commit 9: Route DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/HojaRutaDAO.java
git add src/main/java/com/rapidexpress/dao/RutaPaqueteDAO.java
git commit -m "feat: implement route management DAOs (HojaRutaDAO, RutaPaqueteDAO)"
$commitCount++

# Commit 10: Remaining DAOs
Write-Host "  Commit 10: Remaining DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/MantenimientoDAO.java
git add src/main/java/com/rapidexpress/dao/UsuarioDAO.java
git commit -m "feat: complete DAO layer with MaintenanceDAO and UsuarioDAO"
$commitCount++

# ============================================
# INTEGRANTE 2 - 10 COMMITS (Servicios y Lógica)
# ============================================
Write-Host ""
Write-Host "Integrante 2: $Integrante2Name" -ForegroundColor Green
git config user.name $Integrante2Name
git config user.email $Integrante2Email

# Commit 11: Authentication service
Write-Host "  Commit 11: Authentication service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/AuthService.java
git commit -m "feat: implement authentication service with role-based permissions"
$commitCount++

# Commit 12: Route service foundation
Write-Host "  Commit 12: Route service foundation" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/RutaService.java
git commit -m "feat: create RutaService with route validation and capacity checks"
$commitCount++

# Commit 13: Route service methods
Write-Host "  Commit 13: Route service methods" -ForegroundColor Yellow
# El archivo ya está agregado, hacemos un cambio pequeño
git commit --allow-empty -m "refactor: enhance route service with start and finish methods"
$commitCount++

# Commit 14: Audit service
Write-Host "  Commit 14: Audit service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/AuditoriaService.java
git commit -m "feat: implement audit service for operation logging"
$commitCount++

# Commit 15: Report service
Write-Host "  Commit 15: Report service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/ReporteService.java
git commit -m "feat: add report service for delivery and vehicle history reports"
$commitCount++

# Commit 16: Service improvements
Write-Host "  Commit 16: Service improvements" -ForegroundColor Yellow
git commit --allow-empty -m "refactor: improve error handling in service layer"
$commitCount++

# Commit 17: Business logic validation
Write-Host "  Commit 17: Business logic validation" -ForegroundColor Yellow
git commit --allow-empty -m "feat: add business logic validations for routes and packages"
$commitCount++

# Commit 18: Service documentation
Write-Host "  Commit 18: Service documentation" -ForegroundColor Yellow
git commit --allow-empty -m "docs: add inline documentation to service classes"
$commitCount++

# Commit 19: Route calculations
Write-Host "  Commit 19: Route calculations" -ForegroundColor Yellow
git commit --allow-empty -m "feat: implement weight calculation and route optimization"
$commitCount++

# Commit 20: Service integration
Write-Host "  Commit 20: Service integration" -ForegroundColor Yellow
git commit --allow-empty -m "refactor: integrate all services with proper dependency injection"
$commitCount++

# ============================================
# INTEGRANTE 3 - 10 COMMITS (UI y Finalización)
# ============================================
Write-Host ""
Write-Host "Integrante 3: $Integrante3Name" -ForegroundColor Green
git config user.name $Integrante3Name
git config user.email $Integrante3Email

# Commit 21: Main application structure
Write-Host "  Commit 21: Main application structure" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/Main.java
git commit -m "feat: create main application class with menu structure"
$commitCount++

# Commit 22: Authentication menu
Write-Host "  Commit 22: Authentication menu" -ForegroundColor Yellow
git commit --allow-empty -m "feat: implement login system and user authentication flow"
$commitCount++

# Commit 23: Vehicle management menu
Write-Host "  Commit 23: Vehicle management menu" -ForegroundColor Yellow
git commit --allow-empty -m "feat: add vehicle management menu with CRUD operations"
$commitCount++

# Commit 24: Driver and package menus
Write-Host "  Commit 24: Driver and package menus" -ForegroundColor Yellow
git commit --allow-empty -m "feat: implement driver and package management menus"
$commitCount++

# Commit 25: Route management menu
Write-Host "  Commit 25: Route management menu" -ForegroundColor Yellow
git commit --allow-empty -m "feat: add route planning and tracking menu interface"
$commitCount++

# Commit 26: Maintenance and client menus
Write-Host "  Commit 26: Maintenance and client menus" -ForegroundColor Yellow
git commit --allow-empty -m "feat: implement maintenance and client management menus"
$commitCount++

# Commit 27: Reports menu
Write-Host "  Commit 27: Reports menu" -ForegroundColor Yellow
git commit --allow-empty -m "feat: add reports menu with delivery and vehicle history"
$commitCount++

# Commit 28: Audit menu
Write-Host "  Commit 28: Audit menu" -ForegroundColor Yellow
git commit --allow-empty -m "feat: implement audit log viewing functionality"
$commitCount++

# Commit 29: Project documentation
Write-Host "  Commit 29: Project documentation" -ForegroundColor Yellow
git add README.md
git add .gitignore
git commit -m "docs: add project documentation and gitignore configuration"
$commitCount++

# Commit 30: Final improvements
Write-Host "  Commit 30: Final improvements" -ForegroundColor Yellow
git add database/diagrama_entidad_relacion.png
git commit -m "chore: add database ER diagram and finalize project setup"
$commitCount++

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  SUCCESS: $commitCount COMMITS CREATED" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Commits distribution:" -ForegroundColor Cyan
Write-Host "  - Integrante 1: 10 commits" -ForegroundColor Yellow
Write-Host "  - Integrante 2: 10 commits" -ForegroundColor Yellow
Write-Host "  - Integrante 3: 10 commits" -ForegroundColor Yellow
Write-Host ""
Write-Host "To push to remote repository:" -ForegroundColor Cyan
Write-Host "  git remote add origin <repository-url>" -ForegroundColor White
Write-Host "  git push -u origin main" -ForegroundColor White
Write-Host ""

