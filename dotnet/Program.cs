using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.OpenApi.Models;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllersWithViews();

// Configure CORS policy
builder.Services.AddCors(options =>
{
    options.AddDefaultPolicy(policy =>
    {
        policy.WithOrigins("http://localhost:5207", "http://your-allowed-origin.com") // Replace with your allowed origins
              .AllowAnyMethod()
              .AllowAnyHeader();
    });
});

// Configure Swagger
builder.Services.AddSwaggerGen(c =>
{
    c.SwaggerDoc("v1", new OpenApiInfo
    {
        Title = "Nom de votre API",
        Version = "v1",
        Description = "Documentation API générée par Swagger",
    });
});

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    // Enable Swagger only in development environment
    app.UseSwagger();
    app.UseSwaggerUI(c =>
    {
        c.SwaggerEndpoint("/swagger/v1/swagger.json", "cloud API");
        c.RoutePrefix = string.Empty; // Swagger accessible à la racine
    });
}
else
{
    app.UseExceptionHandler("/Home/Error");
    app.UseHsts();
}

// Use CORS
app.UseCors(); // Activate CORS

app.UseHttpsRedirection();
app.UseStaticFiles();

app.UseRouting();

app.UseAuthorization();

app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");

app.Run();
