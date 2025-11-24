FROM mcr.microsoft.com/dotnet/sdk:2.1-alpine AS build

COPY *.csproj ./app/
COPY . ./app/
WORKDIR /app
RUN dotnet publish -c Release -o out

ENV DATABASE_DIR=./

FROM mcr.microsoft.com/dotnet/aspnet:2.1-alpine AS runtime
WORKDIR /app
COPY --from=build /app/out ./
ENTRYPOINT ["dotnet", "glo4008.Feedback.dll"]