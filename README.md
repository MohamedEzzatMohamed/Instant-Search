# Instant Search - Live Autocomplete Search

A modern, reactive Android application demonstrating a **Live Autocomplete Search** feature powered by the [Rick and Morty API](https://rickandmortyapi.com/).

---

## 💡 Project Overview & Idea

The goal of this project is to implement a real-time, responsive autocomplete search feature using modern Android development practices. As users type in the search bar, the app asynchronously queries the backend with an optimized reactive pipeline, handling debounce, cancellation of obsolete requests, error states, and empty results seamlessly.

---

## 🏗️ Architecture & Core Integration

The project strictly integrates with the project's core infrastructure (`core-cmp` / `core` module) adhering to Clean Architecture principles:

- **Data Layer**:
  - Uses `ApiClient` and `safeApiCall` from `core-cmp` for network execution.
  - Safely handles HTTP status codes, automatically mapping Rick & Morty API's HTTP 404 (no match found) into an empty success state (`State.Success(emptyList())`).
  - Maps data transfer objects (`CharacterDto`) cleanly to domain models (`Character`).

- **Domain Layer**:
  - `SearchCharactersUseCase` inherits from `BaseUseCase<In, Out>` from `core-cmp`.
  - Exposes reactive `Flow<State<List<Character>>>` streams.

- **Presentation Layer**:
  - `SearchViewModel` extends `BaseViewModel<SearchIntent, SearchUiState, SearchEvent>`, maintaining state via `uiStateFlow`.
  - Implements an optimized reactive search pipeline:
    `debounce(300ms)` ➔ `map { it.trim() }` ➔ `distinctUntilChanged()` ➔ `flatMapLatest`
  - `SearchScreen` is decoupled into a stateful container and a stateless `SearchScreenContent` composable built with **Material 3**.

- **Dependency Injection**:
  - Configured with **Koin** (`searchModule`) to inject `HttpClient` created via `HttpClientFactory` and register ViewModel, UseCase, and Repository singletons.

- **Jetpack Compose Previews**:
  - Includes 10 `@Preview` functions covering all 5 state variations (**Idle, Loading, Success, Empty, Error**) across both **Light Mode** and **Dark Mode**.

---

## 🧪 Unit Testing & Quality Assurance

Comprehensive unit test suite created using `kotlinx-coroutines-test`, `MockEngine`, and `JUnit`:
- `SearchViewModelTest`: Verifies query flow updates, intent processing, and state transitions through the reactive pipeline.
- `SearchCharactersRepositoryImplTest`: Verifies API execution via `ApiClient`, DTO mapping, and HTTP 404 empty state handling.
- `SearchCharactersUseCaseTest`: Verifies UseCase execution with `BaseUseCase` and error propagation.

---

## 🤖 Authorship & Acknowledgments

- **Implementation**: Done by **AI Agent**.
- **Review & Validation**: Reviewed, guided, and validated by **Software Engineer**.
