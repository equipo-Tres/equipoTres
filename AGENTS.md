# AGENTS.md — equipoTres (Pico Botella)

## Resumen

App Android de juego "Pico Botella" con splash, home, instrucciones y gestión de retos.

- **Package:** `proyecto.picobotella`
- **Application:** `PicoBotellaApplication` (inyecta repositorios)
- **MainActivity:** `proyecto.picobotella.view.MainActivity`
- **minSdk:** 24 · **compileSdk:** 36 · **JVM:** 11

---

## Reglas estrictas

### MVVM + Repository

```
Fragment → ViewModel → Repository → Room (DAO/Database)
```

| Ejemplo correcto | Ejemplo prohibido |
|------------------|-------------------|
| `RetosFragment` observa `RetosViewModel.allRetos` | Fragment llama a `RetoDao` directamente |
| `RetosViewModel` usa `RetoRepository` | ViewModel crea `RetoDatabase.getDatabase()` |
| `RetoRepository` encapsula `RetoDao` | Repository ausente; DAO usado desde la UI |

Repositorios del proyecto: `RetoRepository`, `AudioRepository`, `RateRepository`.
Inyectar vía `PicoBotellaApplication` + `ViewModelFactory`.

### Corrutinas

- `viewModelScope.launch { }` en ViewModels para insert/delete/seed.
- DAO con funciones `suspend` (`insert`, `delete`, `getCount`).
- `SplashViewModel`: `delay()` dentro de corrutina, no `Thread.sleep()`.

### Room (SQLite)

- Entidad: `model/RetoEntity.kt` (`@Entity`)
- DAO: `data/RetoDao.kt` (`@Dao`)
- Database: `data/RetoDatabase.kt` (`@Database`)
- Seed inicial en `PicoBotellaApplication` con corrutina + `RetoRepository`.

### Fragments

Todas las pantallas son Fragments en `view/fragment/`:

| Fragment | Layout |
|----------|--------|
| `SplashFragment` | `fragment_splash.xml` |
| `HomeFragment` | `fragment_home.xml` |
| `InstructionsFragment` | `fragment_instructions.xml` |
| `RetosFragment` | `fragment_retos.xml` |

`MainActivity` solo carga `activity_main.xml` con el NavHost.

### Navigation Component

- Grafo: `app/src/main/res/navigation/nav_graph.xml`
- Destino inicial: `splashFragment`
- Flujo: splash → home → instructions / retos
- Navegar con `findNavController().navigate(...)` y acciones del grafo
- Package en grafo: `proyecto.picobotella.view.fragment.*`

---

## Estructura de carpetas

```
app/src/main/java/proyecto/picobotella/
├── PicoBotellaApplication.kt
├── data/
│   ├── RetoDao.kt
│   └── RetoDatabase.kt
├── model/
│   └── RetoEntity.kt
├── repository/
│   ├── RetoRepository.kt
│   ├── AudioRepository.kt
│   └── RateRepository.kt
├── viewmodel/
│   ├── SplashViewModel.kt
│   ├── HomeViewModel.kt
│   ├── HomeViewModelFactory.kt
│   ├── RetosViewModel.kt
│   └── RetosViewModelFactory.kt
└── view/
    ├── MainActivity.kt
    ├── OutlinedTextView.kt
    ├── adapter/RetoAdapter.kt
    ├── fragment/ (Splash, Home, Instructions, Retos)
    └── viewholder/RetoViewHolder.kt
```

## Checklist al agregar una feature

- [ ] ¿Nuevo Fragment en `view/fragment/` registrado en `nav_graph.xml`?
- [ ] ¿ViewModel en `viewmodel/` que solo habla con Repository?
- [ ] ¿Repository nuevo o extendido en `repository/`?
- [ ] ¿Datos persistentes vía Room (`model/` + `data/`)?
- [ ] ¿Operaciones async con corrutinas (`viewModelScope`, `suspend`)?
- [ ] ¿Navegación solo con Navigation Component?

## Dependencias clave

Room (KSP), Lifecycle, Coroutines, Navigation Component, Material, Lottie.

## Build

```bash
.\gradlew.bat assembleDebug
```
