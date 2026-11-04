# RFC-SPD-006 Integración de la validación colaborativa

| Campo | Definición |
| --- | --- |
| Registro | 04/11/2026 |
| Responsable | Alejandro Martín Córdova Espinoza (`mysticc101`) |
| Issue | [#15](https://github.com/mysticc101/app-finanzas/issues/15) |
| Tipo y prioridad | Cambio normal de pruebas y documentación; prioridad normal |
| Línea base | `final` en `b84ced3` |
| Rama de trabajo | `qa/validacion-de-movimientos` |
| Estado del registro | Evaluación técnica completada; decisión de integración y cierre conservados en el issue #15 y su pull request |

## Necesidad y propósito

Los aportes de validación del equipo están publicados en una rama compartida. Se solicita reunirlos en una configuración revisada, vincular las evidencias a los cambios y publicar una referencia de versión recuperable. Esta RFC controla la integración y publicación del 4 de noviembre; conserva las fechas originales de los aportes existentes.

## Alcance y elementos afectados

- EC-05.02: BudgetFormRegressionTest y su nueva comprobación de -50 a 0 desde el formulario.
- EC-06: resultados locales del 04/11/2026 y capturas de las pruebas manuales.
- EC-07: RFC, issues, guías y registro de revisión.
- EC-08: identificación y comprobación del APK universal de validación.
- Código productivo y esquema de datos: sin cambios respecto de la línea base.

## Responsabilidades

Jhon aporta la prueba automatizada; Jhostyn documenta el registro y edición de gastos; Dennis documenta transferencias; Marcelo prepara la guía y vincula su revisión con Jhon. Alejandro revisa el conjunto, organiza las evidencias, ejecuta la validación local y administra el pull request y la publicación.

## Recursos y planificación

Se utiliza el entorno existente: Git, GitHub, Android Studio/JDK, SDK 36 y Gradle Wrapper. La integración requiere revisión de los cuatro archivos aportados, compilación, suite local y preparación de evidencias. Estimación de la actividad de integración: una sesión de trabajo de 2 a 4 horas; es una previsión, no una medición de esfuerzo. Las pruebas manuales recibidas se conservan con sus autores y fechas.

## Riesgos y tratamiento

El principal riesgo es atribuir una evidencia a una versión o alcance incorrectos. Se separan las 60 pruebas locales de esta ejecución, las 23 instrumentadas históricas del 11/10/2026 y los dos escenarios manuales. El historial de los participantes se conserva mediante merge, sin squash ni reescritura. La configuración privada de Firebase y el material de firma quedan fuera de Git.

## Criterios de aceptación

1. Aplicación y APK de instrumentación compilados; suite local completa sin fallos.
2. Prueba nueva del presupuesto y resultados manuales coherentes con las evidencias.
3. Registros legibles, imágenes enlazadas y configuración privada excluida.
4. Pull request hacia final revisado y fusionado, conservando los commits de los integrantes.
5. Etiqueta y versión de validación vinculadas con el merge; cierre trazable en GitHub.

## Recuperación

Si falla una comprobación, detener la integración y registrar el defecto. Si se detecta una regresión después de fusionar, evaluar un nuevo commit de reversión y repetir las pruebas afectadas. No se modifica ni elimina la base de datos del usuario. No fue necesario ejecutar una reversión durante la revisión técnica.

## Evidencias de evaluación

[Revisión de integración](REVISION-INTEGRACION.md), [resultados locales](evidencias/resultados-2026-11-04.json) y [confirmación de la guía](https://github.com/mysticc101/app-finanzas/issues/14#issuecomment-6634877605).
