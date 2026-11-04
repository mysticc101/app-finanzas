# Spendly

Aplicación Android de finanzas personales con registro de ingresos, gastos, presupuestos y transferencias entre cuentas.

## Documentación técnica

- [Mantenimiento y refactorizaciones](docs/maintenance/2026-10-11/README.md).
- [Inventario de configuración](docs/maintenance/2026-10-11/CONFIGURACION.md).
- [Validación colaborativa y evidencias](docs/validacion/README.md).
- [Guía para ejecutar la prueba de presupuesto](docs/validacion/presupuesto.md).

## Preparación y compilación

Abrir el proyecto en Android Studio con Android SDK Platform 36 y un JDK compatible con el Gradle Wrapper. Incorporar de forma privada `app/google-services.json`, correspondiente a `com.example.finanzas`, y configurar la ruta del SDK en `local.properties`. Ambos archivos se excluyen de Git.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

El APK debug se genera en `app/build/outputs/apk/debug/Spendly-debug.apk`. Para compartirlo entre teléfonos, utilizar el APK producido por `assembleDebug` y comprobar sus arquitecturas y que no contenga la marca `testOnly`; el botón Run puede producir un artefacto específico del dispositivo de desarrollo.

La aplicación declara Android 8 o superior, versionName 1.0.0 y versionCode 7. La etiqueta de validación identifica el conjunto de código, pruebas y documentación del repositorio; no modifica esos valores ni representa una publicación en Google Play.
