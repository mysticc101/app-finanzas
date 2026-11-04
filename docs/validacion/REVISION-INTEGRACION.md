# Revisión de integración de Spendly

Responsable: Alejandro Martín Córdova Espinoza (`mysticc101`). Fecha: 04/11/2026. Solicitud: [RFC-SPD-006](RFC-SPD-006.md), [issue #15](https://github.com/mysticc101/app-finanzas/issues/15).

## Configuración examinada

Se revisó la rama `qa/validacion-de-movimientos` en `f6416f6d6c9b286e8d78e137460bf72e209f84d2`, con base en `b84ced3`. Los aportes modifican una clase de pruebas y tres documentos; el código productivo permanece idéntico a la base. La revisión de integración organiza las capturas, mejora el formato de las fichas y añade trazabilidad y resultados reproducibles.

## Resultados

Comando ejecutado desde la raíz:

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --console=plain
```

Gradle terminó con `BUILD SUCCESSFUL`. Se ejecutaron **60 pruebas locales, 0 fallos, 0 errores y 0 omitidas**: 42 pruebas de lógica y 18 con Robolectric, incluida `negativeBudgetSavesAsZero`. Los [XML](evidencias/locales-2026-11-04/) y el [log](evidencias/compilacion-locales-2026-11-04.log) conservan el detalle. La compilación de instrumentación no equivale a ejecutarla: las 23 instrumentadas disponibles corresponden al 11/10/2026 y no se repitieron en esta revisión.

El caso de presupuesto captura la llamada al ViewModel y verifica un importe cero, una única invocación y conservación de año, mes y moneda. No comprueba por sí solo el almacenamiento real.

Las capturas de Jhostyn muestran 100 → 80 → 75 soles al registrar y editar un gasto. Las de Dennis muestran efectivo 90, tarjeta 10 y total 100 tras transferir 10 soles. La guía de Marcelo conserva la confirmación de Jhon en el issue #14.

## Correspondencia de fuentes

La validación se ejecutó sobre el commit indicado. El aporte de revisión añade únicamente documentación y evidencias: los archivos `app/`, `gradle/` y los scripts Gradle conservan el contenido validado. El índice de hashes identifica los archivos de código, pruebas y configuración utilizados en esta ejecución.

## Decisión técnica

Las comprobaciones permiten incorporar los aportes mediante un pull request dirigido a `final`. El registro de la fusión y la publicación de la versión se conserva en el issue #15 y en GitHub. La decisión corresponde al administrador del repositorio; no se atribuye una aprobación a terceros.

## Límites y pendientes

Las pruebas manuales cubren los escenarios descritos, no toda la aplicación ni todos los modelos Android. No se verificó sincronización real con Firebase, rendimiento ni porcentaje de cobertura. ACT-02 y ACT-06 conservan su alcance planificado; ACT-08 no supone una actualización general de dependencias. Se recomienda automatizar la compilación y la suite en CI y ampliar la matriz de dispositivos en mantenimiento posterior.
