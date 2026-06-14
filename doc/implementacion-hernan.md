# Documentación de implementación — Pico Botella

**Proyecto:** Miniproyecto 1 — Sprint 1  
**App:** Juego Pico Botella  
**Universidad del Valle** — Desarrollo de Aplicaciones para Dispositivos Móviles  
**Rama:** `Hernan-H.U-3.0-criterio-2-y-H.u-4.0-criterio-1`  
**Responsable de esta entrega:** Hernán (HU 3.0 Criterio 2 + HU 4.0 Criterio 1 + refactor arquitectura)

---

## 1. Resumen ejecutivo

Este documento describe todo lo implementado y refactorizado en la aplicación **Pico Botella** hasta la fecha, incluyendo:

- Historias de Usuario completadas por Hernán.
- Refactorización a arquitectura **MVVM + Repository + corrutinas**.
- Configuración de Gradle para Lifecycle, Room y KSP.
- Estructura de carpetas preparada para el resto del equipo.

La app permite: splash animado → pantalla principal (home) → calificar la app abriendo Google Play con la ficha de **Nequi** (simulación de calificación).

---

## 2. Historias de Usuario implementadas

### HU 1.0 — Ventana Splash (criterios 1–6) ✅ Equipo

| Criterio | Descripción | Estado |
|----------|-------------|--------|
| C1 | Fondo negro, sin toolbar nativa | ✅ |
| C2 | Ícono/botella animada | ✅ |
| C3 | Texto naranja "Pico Botella" | ✅ |
| C4 | Mostrar 5 segundos y pasar al home | ✅ |
| C5 | Botón atrás desde home no vuelve al splash | ✅ (`popUpTo` inclusive) |
| C6 | Ícono de la app personalizado | ✅ |

**Refactor Hernán (Parte 5):** el delay de 5 segundos pasó de `Handler` a **corrutina** en `SplashViewModel`.

---

### HU 2.0 — Ventana Home Principal (criterios 1–6) ✅ Equipo

| Criterio | Descripción | Estado |
|----------|-------------|--------|
| C1 | Fondo de madera | ✅ |
| C2 | Toolbar personalizada (no ActionBar Android) | ✅ |
| C3 | Íconos naranja en toolbar | ✅ |
| C4 | Botella central | ✅ |
| C5 | Contador regresivo 3→0 | ✅ UI estática (valor "3" en XML) |
| C6 | Botón parpadeante "Presióname" | ✅ |
| C7 | Sonido de fondo | ⏳ Pendiente (otro integrante) |

---

### HU 3.0 — Toolbar personalizada

| Criterio | Descripción | Responsable | Estado |
|----------|-------------|-------------|--------|
| C1 | Toolbar negra, bordes redondeados, íconos naranja | Equipo | ✅ |
| **C2** | **Estrella → calificar app (HU 4.0)** | **Hernán** | **✅** |
| C3 | Toggle audio ON/OFF | Pendiente | ⏳ |
| C4 | Info → instrucciones | Pendiente | ⏳ |
| C5 | Retos → lista retos | Pendiente | ⏳ |
| C6 | Compartir app | Pendiente | ⏳ |
| C7 | Animación touch en botones toolbar | Equipo | ✅ (ripple del sistema en XML) |

**Implementación HU 3.0 C2:** al pulsar `btnStar` (estrella), se dispara la acción de calificar vía `HomeViewModel` → `RateRepository` → Google Play.

---

### HU 4.0 — Calificar la aplicación

| Criterio | Descripción | Responsable | Estado |
|----------|-------------|-------------|--------|
| **C1** | **Abrir Play Store con app Nequi como simulación** | **Hernán** | **✅** |

**URL de respaldo (HU oficial):**  
`https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es`

**Paquete Nequi:** `com.nequi.MobileApp`

**Flujo:** estrella → intent Play Store → si no hay app de Play Store instalada, abre la URL web.

---

## 3. Arquitectura del proyecto

### 3.1 Patrón adoptado (clases 10 y 11)

```
┌─────────────────────────────────────────────────────────────┐
│                         VIEW (UI)                           │
│  SplashFragment          HomeFragment                       │
│  (animaciones XML)       (animación btnSpin, clicks UI)     │
└───────────────┬─────────────────────┬───────────────────────┘
                │ LiveData            │ LiveData
                ▼                     ▼
┌───────────────────────┐   ┌───────────────────────┐
│   SplashViewModel     │   │    HomeViewModel      │
│   viewModelScope      │   │    + Factory          │
│   delay(5000)         │   │    onStarClicked()    │
└───────────────────────┘   └───────────┬───────────┘
                                        │
                                        ▼
                            ┌───────────────────────┐
                            │   RateRepository      │
                            │   createPlayStore...  │
                            │   createWebFallback.. │
                            └───────────────────────┘
                                        │
                                        ▼
                            ┌───────────────────────┐
                            │  Google Play / Browser│
                            │  (Intent Nequi)       │
                            └───────────────────────┘
```

### 3.2 Clase Application

`PicoBotellaApplication` centraliza instancias compartidas:

- `rateRepository` — acceso lazy al repositorio de calificación.
- Registrada en `AndroidManifest.xml` con `android:name=".PicoBotellaApplication"`.

### 3.3 Estructura de paquetes

```
proyecto.picobotella/
├── MainActivity.kt
├── PicoBotellaApplication.kt
├── data/
│   ├── model/              ← vacía (retos — otro integrante)
│   ├── local/              ← vacía (Room DAO/DB — otro integrante)
│   └── repository/
│       └── RateRepository.kt
└── ui/
    ├── splash/
    │   ├── SplashFragment.kt
    │   └── SplashViewModel.kt
    └── home/
        ├── HomeFragment.kt
        ├── HomeViewModel.kt
        └── HomeViewModelFactory.kt
```

> **Nota:** Git no versiona carpetas vacías. `data/model/` y `data/local/` existen localmente para que el compañero de retos sepa dónde implementar Room.

---

## 4. Archivos Kotlin — detalle

### 4.1 `MainActivity.kt`

- Actividad contenedora mínima.
- Carga `activity_main.xml` con `NavHostFragment`.
- No contiene lógica de negocio.

### 4.2 `PicoBotellaApplication.kt`

```kotlin
val rateRepository by lazy { RateRepository(this) }
```

- Patrón **lazy**: se crea solo cuando se usa por primera vez.
- Punto de acceso global para repositorios (sin librerías de inyección de dependencias).

### 4.3 `RateRepository.kt`

**Paquete:** `proyecto.picobotella.data.repository`

| Método | Función |
|--------|---------|
| `createPlayStoreIntent()` | Intent `market://details?id=com.nequi.MobileApp` vía app Play Store |
| `createWebFallbackIntent()` | Intent con URL web de Nequi si no hay Play Store |

Usa strings de `res/values/strings.xml`:
- `nequi_package`
- `play_store_package` (`com.android.vending`)
- `play_store_url`

### 4.4 `SplashViewModel.kt`

- Extiende `ViewModel`.
- En `init`, lanza corrutina con `viewModelScope.launch { delay(5000) }`.
- Expone `navigateToHome: LiveData<Boolean>` cuando debe navegarse al home.
- Reemplaza el antiguo `Handler(Looper.getMainLooper()).postDelayed`.

### 4.5 `SplashFragment.kt`

- Infla `fragment_splash.xml`.
- Aplica animación `bottle_animation` a la botella (solo UI).
- Observa `viewModel.navigateToHome` con **`viewLifecycleOwner`**.
- Navega a `homeFragment` con `NavOptions.popUpTo(splashFragment, inclusive = true)`.

### 4.6 `HomeViewModel.kt`

- Recibe `RateRepository` por constructor.
- `onStarClicked()` publica Intent en `_openPlayStore` (LiveData).
- `getWebFallbackIntent()` delega al repository.

### 4.7 `HomeViewModelFactory.kt`

- Implementa `ViewModelProvider.Factory`.
- Crea `HomeViewModel` inyectando `RateRepository` desde `PicoBotellaApplication`.

### 4.8 `HomeFragment.kt`

- Obtiene ViewModel: `by viewModels { HomeViewModelFactory(app.rateRepository) }`.
- **UI:** animación `button_pulse` en botón girar (`btnSpin`) — sin cambios de lógica.
- **Estrella:** `viewModel.onStarClicked()` al click.
- **Observer:** `openPlayStore` → `startActivity(intent)` con fallback web en `ActivityNotFoundException`.
- **No modifica** otros botones de toolbar (audio, info, retos, share).

---

## 5. Recursos XML y navegación

### 5.1 Layouts

| Archivo | Uso |
|---------|-----|
| `activity_main.xml` | `FragmentContainerView` + NavHost |
| `fragment_splash.xml` | Fondo negro, botella, título naranja |
| `fragment_home.xml` | Fondo madera, toolbar, botella, contador, botón girar |
| `toolbar_custom.xml` | CardView negra, 5 ImageButton naranjas |

### 5.2 Animaciones

| Archivo | Uso |
|---------|-----|
| `bottle_animation.xml` | Entrada splash (scale + alpha) |
| `button_pulse.xml` | Parpadeo infinito botón "Presióname" |

### 5.3 Navegación (`nav_graph.xml`)

```
splashFragment  ──(5 s)──►  homeFragment
```

- `startDestination`: `splashFragment`
- Existe `action_splash_to_home` (no usada directamente en código; navegación por ID `homeFragment`).

### 5.4 Strings relevantes

```xml
<string name="play_store_url">https://play.google.com/store/apps/details?id=com.nequi.MobileApp&amp;hl=es_419&amp;gl=es</string>
<string name="play_store_package">com.android.vending</string>
<string name="nequi_package">com.nequi.MobileApp</string>
```

---

## 6. Configuración Gradle (Parte 1)

### 6.1 Dependencias agregadas

| Librería | Propósito |
|----------|-----------|
| `lifecycle-viewmodel-ktx` | ViewModel + `by viewModels()` |
| `lifecycle-livedata-ktx` | LiveData |
| `lifecycle-runtime-ktx` | Ciclo de vida |
| `kotlinx-coroutines-android` | Corrutinas |
| `room-runtime`, `room-ktx` | Base SQLite (equipo retos) |
| `room-compiler` (KSP) | Generación código Room |

### 6.2 Plugins

- `com.android.application`
- `com.google.devtools.ksp` (Room)

**Importante (AGP 9):** no se aplica `org.jetbrains.kotlin.android` porque Kotlin viene integrado en AGP 9. Aplicarlo duplicado causa error de extensión.

### 6.3 `gradle.properties`

```properties
android.disallowKotlinSourceSets=false
```

Necesario para compatibilidad KSP + Kotlin integrado en AGP 9.

---

## 7. Fases de refactorización realizadas

| Fase | Contenido | Resultado |
|------|-----------|-----------|
| **1** | Gradle: Lifecycle, corrutinas, Room, KSP | Proyecto compila con nuevas libs |
| **2** | `PicoBotellaApplication` + Manifest | App arranca con Application custom |
| **3** | Carpetas `data/model`, `data/local`, `data/repository` | Estructura para equipo |
| **4** | `RateRepository` + uso en Home | Lógica Play Store fuera del Fragment |
| **5** | `SplashViewModel` + refactor Splash | Corrutinas en lugar de Handler |
| **6** | `HomeViewModel` + Factory + refactor Home | MVVM completo en calificar |
| **Limpieza** | Mover `RateRepository` a `data/repository/` | Package alineado con carpeta |

---

## 8. Flujos de usuario

### 8.1 Inicio de la app

1. Usuario abre la app → `MainActivity`.
2. `SplashFragment` muestra botella animada + texto "Pico Botella".
3. Tras 5 segundos (`SplashViewModel`) → navega a `HomeFragment`.
4. Splash se elimina del back stack (no se puede volver con botón atrás).

### 8.2 Calificar app (Hernán)

1. Usuario en Home pulsa **estrella** (`btnStar`).
2. `HomeViewModel.onStarClicked()` → `RateRepository.createPlayStoreIntent()`.
3. LiveData notifica al Fragment → `startActivity`.
4. Se abre Google Play en ficha de **Nequi**.
5. Si no hay Play Store → URL web de Nequi.
6. Usuario pulsa atrás → vuelve al Home.

---

## 9. Lo que NO está implementado (otros integrantes)

| Feature | HU | Carpeta sugerida |
|---------|-----|------------------|
| Room (Entity, DAO, Database) | 6–9 | `data/model/`, `data/local/` |
| Lista retos, diálogos CRUD | 6–9 | `ui/retos/` |
| Audio fondo + toggle | 2.0 C7, 3.0 C3 | `HomeViewModel` + assets |
| Instrucciones | 5.0 | `ui/instrucciones/` |
| Compartir app | 10 | Intent share |
| Giro botella + reto aleatorio | 11–12 | `HomeViewModel` + API Pokémon |

Las dependencias de **Room** ya están en Gradle listas para quien implemente retos.

---

## 10. Git y repositorio

- **Rama de trabajo:** `Hernan-H.U-3.0-criterio-2-y-H.u-4.0-criterio-1`
- **Último commit relevante:** refactor MVVM + HU 3.0 C2 + HU 4.0 C1
- **`.gitignore`:** incluye carpeta `contexto/` (material del curso, no se sube al repo)

---

## 11. Criterios de evaluación (rúbrica) — estado parcial

| Requisito RA-1 | Estado en proyecto |
|----------------|-------------------|
| MVVM + Repository | ✅ Splash y Home (calificar) |
| Corrutinas | ✅ SplashViewModel |
| Room SQLite | ⏳ Deps listas; implementación pendiente (retos) |
| Fragments | ✅ |
| Navigation | ✅ |
| Funcionamiento HUs | ⚠️ Parcial (sprint en curso) |

---

## 12. Cómo probar localmente

1. Abrir proyecto en Android Studio.
2. **Sync Project with Gradle Files**.
3. Ejecutar en emulador o dispositivo.
4. Verificar:
   - Splash ~5 s con animación.
   - Home con fondo madera y toolbar.
   - Estrella → Google Play (Nequi).
   - Botón Presióname parpadea.
   - Atrás desde Home → sale de la app.

---

## 13. Referencias del curso

- Clases 8: Fragments, Navigation, diálogos.
- Clases 10–11: MVVM, LiveData, Repository, Room, corrutinas.
- Documento HU: `contexto/HU_pico_botella_proyecto1.pdf` (local, no versionado).

---

*Documento generado para sustentación y handoff al equipo. Actualizar cuando se integren nuevas HUs.*
