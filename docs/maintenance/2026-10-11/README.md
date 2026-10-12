# Mantenimiento y validación de Spendly

Este documento reúne los cambios de mantenimiento de Spendly, sus resultados y los enlaces para comprobarlos. Las correcciones conservan las reglas de montos de cada pantalla, separan validaciones, restauran iconos y colores e integran el repositorio en las operaciones del formulario de transacciones.

La validación del **11 de octubre de 2026** obtuvo **59 pruebas locales y 23 instrumentadas aprobadas**, además de la compilación debug. El código comprobado está en el commit [87b1941](https://github.com/mysticc101/app-finanzas/commit/87b19419b31762d8541a3ae4ae2aca5a553aabe2); la versión inicial utilizada para comparar es [2394201](https://github.com/mysticc101/app-finanzas/commit/2394201a8eaf9c606c49cc4c8b155ca5e2402737). Un commit identifica una versión registrada en Git y permite consultar sus archivos y diferencias.

Para revisar el trabajo, se pueden consultar los resultados siguientes, el [inventario de configuración](CONFIGURACION.md) y la tabla de solicitudes de cambio. Los [resultados XML y registros de ejecución](evidencias/) contienen la evidencia detallada.

## Resultados ejecutados

| Comando desde la raíz | Resultado |
|---|---|
| `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest :app:assembleDebugAndroidTest --console=plain` | BUILD SUCCESSFUL, aplicación debug y APK de pruebas. |
| `.\gradlew.bat :app:testDebugUnitTest --console=plain` | 59 pruebas, 0 fallos, 0 errores, 0 omitidas. |
| `.\gradlew.bat :app:assembleDebugAndroidTest --console=plain` | BUILD SUCCESSFUL con las tres pruebas nuevas del adaptador. |
| `$env:ANDROID_SERIAL='emulator-5580'; .\gradlew.bat :app:connectedDebugAndroidTest --console=plain` | Ejecución final: 23 pruebas, 0 fallos, 0 errores, 0 omitidas. |

Las 59 pruebas locales se ejecutan en el equipo de desarrollo: 42 comprueban lógica con JUnit y 17 utilizan Robolectric para probar componentes Android. Las 23 instrumentadas se ejecutaron en un emulador Android e incluyen 20 pruebas existentes y tres nuevas del adaptador del repositorio.

En el primer intento instrumentado aprobaron 22 de 23 casos. Una prueba nueva buscaba el saldo de efectivo en `accountBalances`, aunque el modelo lo guarda en `HomeSummary.efectivo`. Se corrigió esa consulta en la prueba y se repitieron los 23 casos, todos aprobados. La lógica de saldos de la aplicación permaneció igual. Se conservan los resultados del primer intento y de la ejecución final.

Entorno utilizado: Windows; OpenJDK 21.0.10 de Android Studio; Gradle 8.13; Java/JVM 17; compile SDK 36, target 35 y min 26. Robolectric 4.16.1 con API 28. Instrumentación: emulador SpendlyValidation, Android 17/API 37, imagen android-37.2, x86_64.

Para repetir los comandos, `JAVA_HOME` debe apuntar al JDK y el SDK de Android debe estar configurado. La instrumentación requiere un emulador activo; `emulator-5580` es el identificador usado en esta ejecución y debe sustituirse si el dispositivo disponible tiene otro identificador. Los archivos de configuración privada de servicios deben estar disponibles en el entorno local. Las marcas de tiempo de los resultados XML corresponden a UTC.

## Alcance comprobado

- Montos y presupuesto: formatos, signos, valores fuera de rango y rechazo de `1.2.3` antes del guardado.
- Formulario: registro, edición, transferencias, consulta y eliminación. Las pruebas sustituyen el repositorio por uno de prueba que permite controlar cuándo responde; así comprueban la espera, los errores y la recuperación del formulario.
- Iconos y colores: cinco pruebas comparan el resultado actual con `LegacyCategoryVisuals`, una copia del código inicial utilizada solo como referencia de prueba. Se recorren 87 nombres o palabras clave, sus combinaciones, ingresos y gastos, acentos, tema nocturno y personalización. La comparación utiliza identificadores de recursos y valores de color.
- Persistencia: las pruebas del adaptador recorren `TransactionRepository → TransService → LocalRepository → Room`. Comprueban creación, consulta, actualización y eliminación de movimientos, transferencias y saldos insuficientes. También verifican que las respuestas lleguen al hilo principal de Android después de que la llamada haya devuelto el control.

El alcance comprende pruebas automatizadas y una compilación debug para desarrollo. No incluye navegación manual completa, sincronización real con Firebase, comparación de capturas ni compatibilidad con todos los dispositivos Android. Tampoco se midieron rendimiento o cobertura de líneas de código, ni se publicó una versión de la aplicación para usuarios finales.

## Indicadores estructurales

| Indicador y ámbito | Base 2394201 | Código validado |
|---|---:|---:|
| Llamadas directas de NuevaTransaccionFragment a las seis operaciones de TransService | 6 | 0 |
| Llamadas de ese formulario mediante getRepository() | 0 | 6 |
| Puntos de conversión de montos en las tres pantallas intervenidas | 3 rutinas locales | 1 utilidad común y delegación por pantalla |

Estos indicadores muestran dónde se concentra la lógica y cómo accede el formulario a los datos. La separación de responsabilidades es parcial: el formulario conserva tareas de interfaz y crea el repositorio predeterminado cuando no se le proporciona otro. Las etiquetas, recurrencias y categorías mantienen sus servicios existentes.

## Trazabilidad

Cada RFC (solicitud de cambio) describe el problema, el alcance y los criterios para aceptar el resultado. Su issue asociado es el registro de seguimiento en GitHub, donde se consultan responsables, comentarios y estado. Los códigos ACT corresponden a las actividades del plan de mantenimiento.

| RFC | Issue | Actividad del plan |
|---|---|---|
| [RFC-SPD-001](rfc/RFC-SPD-001.md) | [#9](https://github.com/mysticc101/app-finanzas/issues/9) | ACT-01: centralizar montos; ACT-07: pruebas de regresión. |
| [RFC-SPD-002](rfc/RFC-SPD-002.md) | [#10](https://github.com/mysticc101/app-finanzas/issues/10) | ACT-03: separar validaciones; ACT-07: pruebas de regresión. |
| [RFC-SPD-003](rfc/RFC-SPD-003.md) | [#11](https://github.com/mysticc101/app-finanzas/issues/11) | ACT-04: organizar reglas visuales; ACT-07: pruebas de regresión. |
| [RFC-SPD-004](rfc/RFC-SPD-004.md) | [#12](https://github.com/mysticc101/app-finanzas/issues/12) | ACT-05: desacoplar el acceso a datos; ACT-07: pruebas de regresión. |
| [RFC-SPD-005](rfc/RFC-SPD-005.md) | [#13](https://github.com/mysticc101/app-finanzas/issues/13) | Documentar configuración y relacionar cambios, código y pruebas. |

Las correcciones técnicas y sus pruebas precedieron a la creación de las RFC y los issues. GitHub conserva las fechas reales de registro, publicación y cierre. El commit [af963a3](https://github.com/mysticc101/app-finanzas/commit/af963a3eac4760cbf017060efc85a228b7f3768d) reúne el registro inicial de las cinco RFC, el inventario y las evidencias. Las revisiones posteriores de estos documentos se pueden consultar en su historial de Git.

## Integridad y privacidad de la evidencia

Los resultados XML conservan nombres de pruebas, cantidades, duraciones, fechas y fallos. Los registros publicados omiten datos personales del equipo. [procedencia.json](evidencias/procedencia.json) contiene los identificadores SHA-256 de los archivos originales y publicados; estos identificadores permiten detectar cambios en su contenido. [fuentes.json](evidencias/fuentes.json) relaciona los 29 archivos validados con el código del commit, considerando la conversión de saltos de línea que realiza Git.

Las pruebas se ejecutaron localmente antes del commit y se comprobó que el código publicado correspondiera al validado. Estos resultados proceden de Gradle en el entorno descrito; no de una ejecución de GitHub Actions.

## Estado del mantenimiento

ACT-01, ACT-03, ACT-04 y ACT-05 están implementadas y comprobadas mediante las pruebas de ACT-07. Permanecen planificadas la centralización de conversiones de densidad de pantalla (ACT-02), la reorganización de las formas de acceso a la base de datos (ACT-06) y la revisión general de dependencias (ACT-08).
