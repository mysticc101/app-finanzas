# Mantenimiento y validación de Spendly

Fecha de validación: 11 de octubre de 2026 (America/Lima). Los timestamps de los XML están en UTC. Código publicado: [87b1941](https://github.com/mysticc101/app-finanzas/commit/87b19419b31762d8541a3ae4ae2aca5a553aabe2). Base de comparación: [2394201](https://github.com/mysticc101/app-finanzas/commit/2394201a8eaf9c606c49cc4c8b155ca5e2402737).

El paquete conserva las reglas de montos por pantalla, separa validaciones, recupera equivalencia visual y conecta las seis operaciones del formulario con un repositorio asíncrono. Las RFC formalizan correcciones ya implementadas localmente; sus fechas de registro no sustituyen la fecha de implementación original.

## Resultados ejecutados

| Comando desde la raíz | Resultado |
|---|---|
| `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --console=plain` | BUILD SUCCESSFUL, aplicación debug y APK de pruebas. |
| `.\gradlew.bat :app:testDebugUnitTest --console=plain` | 59 pruebas, 0 fallos, 0 errores, 0 omitidas. |
| `.\gradlew.bat :app:assembleDebugAndroidTest --console=plain` | BUILD SUCCESSFUL con las tres pruebas nuevas del adaptador. |
| `$env:ANDROID_SERIAL='emulator-5580'; .\gradlew.bat :app:connectedDebugAndroidTest --console=plain` | Ejecución final: 23 pruebas, 0 fallos, 0 errores, 0 omitidas. |

Las 59 pruebas locales se dividen en 42 JUnit y 17 Robolectric. Las 23 instrumentadas incluyen 20 existentes y tres nuevas del adaptador. Se ejecutaron en un AVD temporal independiente y luego se apagó. El primer intento instrumentado obtuvo 22/23: una expectativa nueva buscaba efectivo dentro de accountBalances, aunque el modelo lo expone en HomeSummary.efectivo. Se corrigió la prueba y se repitieron las 23. No se cambió la lógica productiva de saldos por ese fallo.

Entorno: Windows; OpenJDK 21.0.10 de Android Studio; Gradle 8.13; Java/JVM 17; compile SDK 36, target 35 y min 26. Robolectric 4.16.1 con API 28. Instrumentación: emulador SpendlyValidation, Android 17/API 37, imagen instalada android-37.2, x86_64. JAVA_HOME debe apuntar al JDK del entorno; Android SDK y la configuración local de servicios no se publican.

## Alcance comprobado

- Parser y presupuesto: formatos, signos, desbordamientos y rechazo de 1.2.3 antes del guardado.
- Formulario: registro, edición, transferencias, consulta y eliminación utilizando un repositorio diferido inyectado; limpieza de errores y recuperación tras error asíncrono.
- Visuales: cinco pruebas contra LegacyCategoryVisuals, copia de la implementación de 2394201 usada como oráculo. Se recorren 87 nombres/palabras clave, combinaciones, ambos tipos, acentos, tema nocturno y personalización. Se comparan recursos y colores, no capturas de pantalla.
- Persistencia: TransactionRepository → TransService → LocalRepository → Room. CRUD, transferencias, saldos insuficientes y entrega de callbacks en Main después del retorno de la llamada.

No se midió rendimiento ni cobertura de líneas. Estas verificaciones no equivalen a una sesión manual integral, validación de Firebase o certificación de todas las versiones Android. El build es debug; la publicación del código no constituye despliegue ni release firmada.

## Indicadores estructurales

| Indicador y ámbito | Base 2394201 | Código validado |
|---|---:|---:|
| Llamadas directas de NuevaTransaccionFragment a las seis operaciones de TransService | 6 | 0 |
| Llamadas de ese formulario mediante getRepository() | 0 | 6 |
| Puntos de conversión de montos en las tres pantallas intervenidas | 3 rutinas locales | 1 utilidad común y delegación por pantalla |

Son conteos y cambios estructurales; no miden un porcentaje global de mantenibilidad. La separación de SRP e IoC es parcial: el getter mantiene creación de respaldo y etiquetas/recurrencias/categorías conservan sus servicios.

## Trazabilidad

| RFC | Issue | Actividad |
|---|---|---|
| [RFC-SPD-001](rfc/RFC-SPD-001.md) | [#9](https://github.com/mysticc101/app-finanzas/issues/9) | ACT-01 y ACT-07 |
| [RFC-SPD-002](rfc/RFC-SPD-002.md) | [#10](https://github.com/mysticc101/app-finanzas/issues/10) | ACT-03 y ACT-07 |
| [RFC-SPD-003](rfc/RFC-SPD-003.md) | [#11](https://github.com/mysticc101/app-finanzas/issues/11) | ACT-04 y ACT-07 |
| [RFC-SPD-004](rfc/RFC-SPD-004.md) | [#12](https://github.com/mysticc101/app-finanzas/issues/12) | ACT-05 y ACT-07 |
| [RFC-SPD-005](rfc/RFC-SPD-005.md) | [#13](https://github.com/mysticc101/app-finanzas/issues/13) | Control de configuracion y trazabilidad transversal |

Los estados y fechas de cierre se verifican en los issues. El inventario está en [CONFIGURACION.md](CONFIGURACION.md).

## Integridad y privacidad de la evidencia

Los XML publicados conservan los casos, cantidades, duraciones, timestamps y fallos. Se omiten propiedades del entorno, hostname y salidas auxiliares; los logs sustituyen rutas personales. [procedencia.json](evidencias/procedencia.json) registra las huellas de originales privados y copias depuradas. [fuentes.json](evidencias/fuentes.json) vincula los 29 archivos validados con los blobs del commit, teniendo en cuenta la normalización de finales de línea de Git.

Los resultados pertenecen a ejecuciones anteriores al commit; se comprobó su correspondencia con el código publicado. No se presentan como una ejecución nueva ni como CI de GitHub.

ACT-01, ACT-03, ACT-04 y ACT-05 están implementadas y verificadas en el alcance indicado. ACT-07 tiene regresión automatizada verificada. ACT-02, la unificación estructural de ACT-06 y la actualización general de ACT-08 se mantienen como trabajo planificado independiente.
