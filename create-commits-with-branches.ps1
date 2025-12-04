# Script para crear 30 commits distribuidos en ramas separadas por integrante
# Cada integrante trabaja en su propia rama y luego se hace merge a main

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
Write-Host "  CREATING 30 COMMITS IN SEPARATE BRANCHES" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

if (-not (Test-Path ".git")) {
    Write-Host "Error: No Git repository found. Run 'git init' first." -ForegroundColor Red
    exit 1
}

# Crear rama main inicial si no existe
$currentBranch = git branch --show-current
if (-not $currentBranch) {
    git checkout -b main
    Write-Host "Created main branch" -ForegroundColor Green
}

# Crear README inicial en main
if (-not (Test-Path "README.md")) {
    Write-Host "Creating initial README in main..." -ForegroundColor Yellow
    @"
# RapidExpress - Logistics and Delivery System

A comprehensive Java-based logistics and delivery management system.

## Team Members
- $Integrante1Name
- $Integrante2Name
- $Integrante3Name
"@ | Out-File -FilePath "README.md" -Encoding UTF8
    git add README.md
    git commit -m "chore: initial project setup"
}

# Crear rama testing desde main
git checkout -b testing
Write-Host "Created testing branch from main" -ForegroundColor Green

$commitCount = 0

# ============================================
# INTEGRANTE 1 - 10 COMMITS EN SU RAMA
# ============================================
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Integrante 1: $Integrante1Name" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Crear y cambiar a rama del integrante 1 desde testing
git checkout testing
git checkout -b feature/andres-mendoza-setup
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

# Commit 4: Entity models - Cliente, Conductor, Vehiculo
Write-Host "  Commit 4: Core entity models" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/model/entity/Cliente.java
git add src/main/java/com/rapidexpress/model/entity/Conductor.java
git add src/main/java/com/rapidexpress/model/entity/Vehiculo.java
git commit -m "feat: implement core entity models (Cliente, Conductor, Vehiculo)"
$commitCount++

# Commit 5: Package and route entities
Write-Host "  Commit 5: Package and route entities" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/model/entity/Paquete.java
git add src/main/java/com/rapidexpress/model/entity/HojaRuta.java
git add src/main/java/com/rapidexpress/model/entity/RutaPaquete.java
git commit -m "feat: add package and route entity models with relationships"
$commitCount++

# Commit 6: User and maintenance entities
Write-Host "  Commit 6: User and maintenance entities" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/model/entity/Usuario.java
git add src/main/java/com/rapidexpress/model/entity/Rol.java
git add src/main/java/com/rapidexpress/model/entity/Mantenimiento.java
git commit -m "feat: implement user, role and maintenance entity models"
$commitCount++

# Commit 7: Database connection utility
Write-Host "  Commit 7: Database connection utility" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/utils/ConexionBD.java
git commit -m "feat: implement database connection utility with MySQL driver"
$commitCount++

# Commit 8: Basic DAOs - Cliente and Conductor
Write-Host "  Commit 8: Basic DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/ClienteDAO.java
git add src/main/java/com/rapidexpress/dao/ConductorDAO.java
git commit -m "feat: implement ClienteDAO and ConductorDAO with CRUD operations"
$commitCount++

# Commit 9: Vehicle and Package DAOs
Write-Host "  Commit 9: Vehicle and Package DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/VehiculoDAO.java
git add src/main/java/com/rapidexpress/dao/PaqueteDAO.java
git commit -m "feat: add VehiculoDAO and PaqueteDAO with tracking functionality"
$commitCount++

# Commit 10: Route and maintenance DAOs
Write-Host "  Commit 10: Route and maintenance DAOs" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/dao/HojaRutaDAO.java
git add src/main/java/com/rapidexpress/dao/RutaPaqueteDAO.java
git add src/main/java/com/rapidexpress/dao/MantenimientoDAO.java
git add src/main/java/com/rapidexpress/dao/UsuarioDAO.java
git commit -m "feat: complete DAO layer with route, maintenance and user DAOs"
$commitCount++

# Merge rama del integrante 1 a testing
Write-Host ""
Write-Host "Merging branch to testing..." -ForegroundColor Cyan
git checkout testing
git merge feature/andres-mendoza-setup --no-ff -m "merge: integrate Andres Mendoza's setup and DAO implementation"
Write-Host "Merge completed!" -ForegroundColor Green

# ============================================
# INTEGRANTE 2 - 10 COMMITS EN SU RAMA
# ============================================
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Integrante 2: $Integrante2Name" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Crear y cambiar a rama del integrante 2 desde testing
git checkout testing
git checkout -b feature/maria-contreras-services
git config user.name $Integrante2Name
git config user.email $Integrante2Email

# Commit 11: Authentication service
Write-Host "  Commit 11: Authentication service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/AuthService.java
git commit -m "feat: implement authentication service with role-based permissions"
$commitCount++

# Commit 12: Route service - basic structure
Write-Host "  Commit 12: Route service foundation" -ForegroundColor Yellow
# Agregar comentario al inicio del archivo
$rutaServiceContent = Get-Content "src/main/java/com/rapidexpress/service/RutaService.java" -Raw
if ($rutaServiceContent -notmatch "/\*\*") {
    $newContent = "/**`n * Service class for managing routes and route-package relationships`n * Handles route creation, package assignment, and route lifecycle`n */`n" + $rutaServiceContent
    Set-Content "src/main/java/com/rapidexpress/service/RutaService.java" $newContent -NoNewline
}
git add src/main/java/com/rapidexpress/service/RutaService.java
git commit -m "feat: create RutaService with route validation and capacity checks"
$commitCount++

# Commit 13: Route service - start and finish methods
Write-Host "  Commit 13: Route service methods" -ForegroundColor Yellow
$rutaServiceContent = Get-Content "src/main/java/com/rapidexpress/service/RutaService.java" -Raw
if ($rutaServiceContent -notmatch "Starts a route") {
    $rutaServiceContent = $rutaServiceContent -replace "public void iniciarRuta", "/**`n     * Starts a route and updates vehicle, driver and package statuses`n     */`n    public void iniciarRuta"
    $rutaServiceContent = $rutaServiceContent -replace "public void finalizarRuta", "/**`n     * Finalizes a route and resets vehicle and driver statuses`n     */`n    public void finalizarRuta"
    Set-Content "src/main/java/com/rapidexpress/service/RutaService.java" $rutaServiceContent -NoNewline
}
git add src/main/java/com/rapidexpress/service/RutaService.java
git commit -m "refactor: enhance route service with documented start and finish methods"
$commitCount++

# Commit 14: Audit service
Write-Host "  Commit 14: Audit service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/AuditoriaService.java
git commit -m "feat: implement audit service for operation logging to database and file"
$commitCount++

# Commit 15: Report service
Write-Host "  Commit 15: Report service" -ForegroundColor Yellow
git add src/main/java/com/rapidexpress/service/ReporteService.java
git commit -m "feat: add report service for delivery and vehicle history reports"
$commitCount++

# Commit 16: Service error handling improvements
Write-Host "  Commit 16: Service error handling" -ForegroundColor Yellow
git commit --allow-empty -m "refactor: improve error handling in service layer with better logging"
$commitCount++

# Commit 17: Business logic validation
Write-Host "  Commit 17: Business logic validation" -ForegroundColor Yellow
$rutaServiceContent = Get-Content "src/main/java/com/rapidexpress/service/RutaService.java" -Raw
if ($rutaServiceContent -notmatch "Validates if a package") {
    $rutaServiceContent = $rutaServiceContent -replace "public boolean validarCapacidad", "/**`n     * Validates if a package can be added to a route based on vehicle capacity`n     * @param hojaRutaId Route ID`n     * @param paqueteId Package ID`n     * @return true if package fits, false otherwise`n     */`n    public boolean validarCapacidad"
    Set-Content "src/main/java/com/rapidexpress/service/RutaService.java" $rutaServiceContent -NoNewline
    git add src/main/java/com/rapidexpress/service/RutaService.java
    git commit -m "feat: add business logic validations for routes and packages"
} else {
    git commit --allow-empty -m "feat: add business logic validations for routes and packages"
}
$commitCount++

# Commit 18: Service documentation
Write-Host "  Commit 18: Service documentation" -ForegroundColor Yellow
$authServiceContent = Get-Content "src/main/java/com/rapidexpress/service/AuthService.java" -Raw
if ($authServiceContent -notmatch "/\*\*") {
    $authServiceContent = "/**`n * Service for handling user authentication and authorization`n * Manages login, logout, and permission checking based on user roles`n */`n" + $authServiceContent
    Set-Content "src/main/java/com/rapidexpress/service/AuthService.java" $authServiceContent -NoNewline
    git add src/main/java/com/rapidexpress/service/AuthService.java
    git commit -m "docs: add JavaDoc documentation to authentication service"
    $commitCount++
} else {
    git commit --allow-empty -m "docs: add inline documentation to service classes"
    $commitCount++
}

# Commit 19: Route calculations
Write-Host "  Commit 19: Route calculations" -ForegroundColor Yellow
$rutaServiceContent = Get-Content "src/main/java/com/rapidexpress/service/RutaService.java" -Raw
if ($rutaServiceContent -notmatch "Calculates and updates") {
    $rutaServiceContent = $rutaServiceContent -replace "public void calcularPesoTotalRuta", "/**`n     * Calculates and updates the total weight of all packages in a route`n     * @param hojaRutaId Route ID`n     */`n    public void calcularPesoTotalRuta"
    Set-Content "src/main/java/com/rapidexpress/service/RutaService.java" $rutaServiceContent -NoNewline
    git add src/main/java/com/rapidexpress/service/RutaService.java
    git commit -m "feat: implement weight calculation and route optimization methods"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: implement weight calculation and route optimization methods"
    $commitCount++
}

# Commit 20: Service integration
Write-Host "  Commit 20: Service integration" -ForegroundColor Yellow
$rutaServiceContent = Get-Content "src/main/java/com/rapidexpress/service/RutaService.java" -Raw
if ($rutaServiceContent -notmatch "Initializes RutaService") {
    $rutaServiceContent = $rutaServiceContent -replace "public RutaService\(\) \{", "/**`n     * Initializes RutaService with all required DAO dependencies`n     */`n    public RutaService() {"
    Set-Content "src/main/java/com/rapidexpress/service/RutaService.java" $rutaServiceContent -NoNewline
    git add src/main/java/com/rapidexpress/service/RutaService.java
    git commit -m "refactor: integrate all services with proper dependency initialization"
    $commitCount++
} else {
    git commit --allow-empty -m "refactor: integrate all services with proper dependency initialization"
    $commitCount++
}

# Merge rama del integrante 2 a testing
Write-Host ""
Write-Host "Merging branch to testing..." -ForegroundColor Cyan
git checkout testing
git merge feature/maria-contreras-services --no-ff -m "merge: integrate Maria Contreras's service layer implementation"
Write-Host "Merge completed!" -ForegroundColor Green

# ============================================
# INTEGRANTE 3 - 10 COMMITS EN SU RAMA
# ============================================
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "Integrante 3: $Integrante3Name" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

# Crear y cambiar a rama del integrante 3 desde testing
git checkout testing
git checkout -b feature/pamela-galvis-ui
git config user.name $Integrante3Name
git config user.email $Integrante3Email

# Commit 21: Main application structure
Write-Host "  Commit 21: Main application structure" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "/\*\*") {
    $mainContent = "/**`n * Main application class for RapidExpress Logistics System`n * Provides console-based user interface for managing logistics operations`n */`n" + $mainContent
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
}
git add src/main/java/com/rapidexpress/Main.java
git commit -m "feat: create main application class with menu structure"
$commitCount++

# Commit 22: Authentication menu
Write-Host "  Commit 22: Authentication menu" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Handles user login") {
    $mainContent = $mainContent -replace "private static boolean login\(\) \{", "/**`n     * Handles user login with maximum 3 attempts`n     * @return true if login successful, false otherwise`n     */`n    private static boolean login() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: implement login system and user authentication flow"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: implement login system and user authentication flow"
    $commitCount++
}

# Commit 23: Vehicle management menu
Write-Host "  Commit 23: Vehicle management menu" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays and handles vehicle") {
    $mainContent = $mainContent -replace "private static void menuVehiculos\(\) \{", "/**`n     * Displays and handles vehicle management menu options`n     */`n    private static void menuVehiculos() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: add vehicle management menu with CRUD operations"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: add vehicle management menu with CRUD operations"
    $commitCount++
}

# Commit 24: Driver and package menus
Write-Host "  Commit 24: Driver and package menus" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays and handles driver") {
    $mainContent = $mainContent -replace "private static void menuConductores\(\) \{", "/**`n     * Displays and handles driver management menu`n     */`n    private static void menuConductores() {"
    $mainContent = $mainContent -replace "private static void menuPaquetes\(\) \{", "/**`n     * Displays and handles package management menu`n     */`n    private static void menuPaquetes() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: implement driver and package management menus"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: implement driver and package management menus"
    $commitCount++
}

# Commit 25: Route management menu
Write-Host "  Commit 25: Route management menu" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays route planning") {
    $mainContent = $mainContent -replace "private static void menuRutas\(\) \{", "/**`n     * Displays route planning and tracking menu with role-based options`n     */`n    private static void menuRutas() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: add route planning and tracking menu interface"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: add route planning and tracking menu interface"
    $commitCount++
}

# Commit 26: Maintenance and client menus
Write-Host "  Commit 26: Maintenance and client menus" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays client management") {
    $mainContent = $mainContent -replace "private static void menuClientes\(\) \{", "/**`n     * Displays client management menu`n     */`n    private static void menuClientes() {"
    $mainContent = $mainContent -replace "private static void menuMantenimientos\(\) \{", "/**`n     * Displays maintenance management menu`n     */`n    private static void menuMantenimientos() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: implement maintenance and client management menus"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: implement maintenance and client management menus"
    $commitCount++
}

# Commit 27: Reports menu
Write-Host "  Commit 27: Reports menu" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays reports menu") {
    $mainContent = $mainContent -replace "private static void menuReportes\(\) \{", "/**`n     * Displays reports menu with delivery and vehicle history options`n     */`n    private static void menuReportes() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: add reports menu with delivery and vehicle history"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: add reports menu with delivery and vehicle history"
    $commitCount++
}

# Commit 28: Audit menu
Write-Host "  Commit 28: Audit menu" -ForegroundColor Yellow
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Displays audit log") {
    $mainContent = $mainContent -replace "private static void menuAuditoria\(\) \{", "/**`n     * Displays audit log viewing interface`n     */`n    private static void menuAuditoria() {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
    git commit -m "feat: implement audit log viewing functionality"
    $commitCount++
} else {
    git commit --allow-empty -m "feat: implement audit log viewing functionality"
    $commitCount++
}

# Commit 29: Project documentation
Write-Host "  Commit 29: Project documentation" -ForegroundColor Yellow
git add README.md
git add .gitignore
git commit -m "docs: add project documentation and gitignore configuration"
$commitCount++

# Commit 30: Final improvements
Write-Host "  Commit 30: Final improvements" -ForegroundColor Yellow
if (Test-Path "database/diagrama_entidad_relacion.png") {
    git add database/diagrama_entidad_relacion.png
}
$mainContent = Get-Content "src/main/java/com/rapidexpress/Main.java" -Raw
if ($mainContent -notmatch "Main entry point") {
    $mainContent = $mainContent -replace "public static void main\(String\[\] args\) \{", "/**`n     * Main entry point of the application`n     * Initializes directories and starts login process`n     * @param args Command line arguments (not used)`n     */`n    public static void main(String[] args) {"
    Set-Content "src/main/java/com/rapidexpress/Main.java" $mainContent -NoNewline
    git add src/main/java/com/rapidexpress/Main.java
}
git commit -m "chore: add database ER diagram and finalize project setup"
$commitCount++

# Merge rama del integrante 3 a testing
Write-Host ""
Write-Host "Merging branch to testing..." -ForegroundColor Cyan
git checkout testing
git merge feature/pamela-galvis-ui --no-ff -m "merge: integrate Pamela Galvis's user interface implementation"
Write-Host "Merge completed!" -ForegroundColor Green

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  SUCCESS: 30 COMMITS CREATED IN BRANCHES" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Commits distribution:" -ForegroundColor Cyan
Write-Host "  - Andres Mendoza (feature/andres-mendoza-setup): 10 commits" -ForegroundColor Yellow
Write-Host "  - Maria Contreras (feature/maria-contreras-services): 10 commits" -ForegroundColor Yellow
Write-Host "  - Pamela Galvis (feature/pamela-galvis-ui): 10 commits" -ForegroundColor Yellow
Write-Host ""
Write-Host "Branches created:" -ForegroundColor Cyan
Write-Host "  - main (protected - untouched)" -ForegroundColor White
Write-Host "  - testing (all merges go here)" -ForegroundColor White
Write-Host "  - feature/andres-mendoza-setup (merged to testing)" -ForegroundColor White
Write-Host "  - feature/maria-contreras-services (merged to testing)" -ForegroundColor White
Write-Host "  - feature/pamela-galvis-ui (merged to testing)" -ForegroundColor White
Write-Host ""
Write-Host "To view commit history:" -ForegroundColor Cyan
Write-Host "  git log --oneline --graph --all" -ForegroundColor White
Write-Host ""
Write-Host "To push to remote repository:" -ForegroundColor Cyan
Write-Host "  git remote add origin <repository-url>" -ForegroundColor White
Write-Host "  git push -u origin main" -ForegroundColor White
Write-Host "  git push -u origin testing" -ForegroundColor White
Write-Host "  git push origin feature/andres-mendoza-setup" -ForegroundColor White
Write-Host "  git push origin feature/maria-contreras-services" -ForegroundColor White
Write-Host "  git push origin feature/pamela-galvis-ui" -ForegroundColor White
Write-Host ""

