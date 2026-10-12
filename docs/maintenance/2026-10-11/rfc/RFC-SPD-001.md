# RFC-SPD-001 Preservar las reglas de montos al centralizar AmountParser

| Campo | Registro |
|---|---|
| Registro formal | 2026-10-12T00:37:04Z (UTC), 11/10/2026 en America/Lima |
| Solicitante y autoridad de cambio | Responsable del proyecto, @mysticc101 |
| Responsable técnico y de configuración | @mysticc101 |
| Tipo | Cambio normal; bug, refactor, prioridad:alta |
| Actividad relacionada | ACT-01 y ACT-07 |
| Issue y estado operativo | [#9](https://github.com/mysticc101/app-finanzas/issues/9); consultar su historial para registro y cierre |
| Decisión | Incorporación, publicación y documentación autorizadas por el responsable del proyecto el 11/10/2026 |
| Implementación | [87b1941](https://github.com/mysticc101/app-finanzas/commit/87b19419b31762d8541a3ae4ae2aca5a553aabe2) |

## Descripción y propósito

La centralizacion local convertia negativos del presupuesto a positivos y entradas como 1.2.3 a cero, permitiendo continuar al guardado.

Parser compartido con variante estricta; presupuesto conserva el signo y rechaza formato incorrecto antes de persistir. Home limita negativos a cero y el formulario aplica valor absoluto. Se rechazan desbordamientos no finitos.

## Impacto, alcance y recursos

Componentes: AmountParser.java, PresupuestoFragment.java, HomeFragment.java, NuevaTransaccionFragment.java. Personas afectadas: usuarios de los flujos intervenidos y responsable de mantenimiento. No cambia el esquema Room ni se requiere migración de datos por este paquete.

Cambiar la interpretacion de formatos o signos. Mitigacion: casos de formatos internacionales, negativos, formato incorrecto y desbordamientos.

Recursos empleados: proyecto y equipo existentes, Android SDK/JDK, Gradle, JUnit/Robolectric y emulador. No se registra una estimación histórica de horas para correcciones ya implementadas. La formalización documental se planificó en una sesión; no se presenta como medición de esfuerzo ni costo.

Dependencias: las cuatro correcciones técnicas comparten el formulario y sus pruebas; se publican juntas para conservar un estado validado. La documentación depende de la identificación del commit y los resultados.

## Criterios de aceptación y comprobación

- El presupuesto rechaza 1.2.3 antes del guardado.
- El presupuesto no convierte -50 en +50; Home limita negativos a cero.
- El formulario mantiene valor absoluto y rechaza importes invalidos.

Evidencia: AmountParserRegressionTest, BudgetFormRegressionTest, TransactionFormConsumerTest. Véase el [expediente de validación](../README.md) y sus XML. Los resultados globales son 59 pruebas locales y 23 instrumentadas aprobadas. El estado de esta solicitud distingue la corrección técnica de su formalización administrativa posterior.

## Decisión, recuperación y cierre

La autorización abarca el registro de los cambios y su publicación, sin atribuir aprobación a un comité o revisor independiente. La aceptación de una RFC para análisis no equivale a aprobar su ejecución. Para este paquete, la implementación local y las pruebas preceden al registro formal; el historial de GitHub conserva esa secuencia real.

Si se detecta una regresión, reabrir el issue, adjuntar el caso reproducible y evaluar una corrección o un revert explícito del paquete; validar antes de publicar y conservar los datos del usuario. No se ejecutó un rollback como parte de esta validación.

El cierre requiere comprobar criterios, enlaces, publicación y evidencia. La fecha y el actor efectivos del cierre quedan en GitHub, sin anticiparlos ni reemplazarlos por fechas históricas.
