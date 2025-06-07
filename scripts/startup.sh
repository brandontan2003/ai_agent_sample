unset GOOGLE_GENAI_USE_VERTEXAI
unset GOOGLE_API_KEY

echo "✅ Environment variables have been cleared."
echo "GOOGLE_GENAI_USE_VERTEXAI: $GOOGLE_GENAI_USE_VERTEXAI"
echo "GOOGLE_API_KEY: $GOOGLE_API_KEY"

export GOOGLE_GENAI_USE_VERTEXAI=FALSE
export GOOGLE_API_KEY=

echo "✅ Environment variables have been set."
echo "GOOGLE_GENAI_USE_VERTEXAI: $GOOGLE_GENAI_USE_VERTEXAI"
echo "GOOGLE_API_KEY: $GOOGLE_API_KEY"

mvn exec:java \
    -Dexec.mainClass="com.google.adk.web.AdkWebServer" \
    -Dexec.args="--adk.agents.source-dir=src/main/java" \
    -Dexec.classpathScope="compile"