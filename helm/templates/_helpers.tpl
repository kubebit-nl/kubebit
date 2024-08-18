
{{/* Kubernetes standard labels */}}

{{- define "common.labels.core" -}}
app.kubernetes.io/name: {{ .Release.Name }}-core
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/component: core
{{- end -}}