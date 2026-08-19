# Bibliografía / Recursos

## Git

### Configuración (una sola vez por computadora)

```
git config --global user.name "Nombre Apellido"
git config --global user.email "correo@ejemplo.com"
git config --global core.editor "code --wait"
git config --global pull.rebase false
gh auth login
```

### El ciclo de trabajo

```
| `git clone <url>` | Descarga un repositorio remoto con todo su historial |
| `git status` | Dice en qué estado está todo. **Ejecutarlo después de cada paso** |
| `git add <archivo>` | Prepara cambios para el próximo commit (`git add .` prepara todo) |
| `git commit -m "mensaje"` | Confirma los cambios preparados en el historial local |
| `git log --oneline --graph --all` | Muestra el historial en forma compacta |
| `git diff` | Muestra qué cambió, línea por línea |
| `git push` | Sube los commits locales al repositorio remoto |
| `git pull` | Baja e integra los commits del remoto |
| `git switch -c <rama>` | Crea una rama y se para en ella (`git switch <rama>` para cambiar) |
| `git merge <rama>` | Fusiona la rama indicada en la rama actual |
```

### Resolver un conflicto


```
git pull                    # avisa: CONFLICT (content): Merge conflict in ...
git status                  # muestra: both modified: <archivo>
# editar el archivo, decidir qué queda y borrar <<<<<<<, ======= y >>>>>>>
git add <archivo>
git commit -m "Resuelve el conflicto en <archivo>"
git push
```

## Vec2
- [Demostración interactiva de un vector normalizado](https://www.desmos.com/calculator/1qcghxneg5)

## Patrón de diseño factory
- <https://www.arquitecturajava.com/patron-factory-para-que-sirve/>

## Patrón de diseño builder
- <https://refactoring.guru/es/design-patterns/builder>

## General
- Deitel, P., & Deitel, H. (2017). Java How to Program, Early Objects.
