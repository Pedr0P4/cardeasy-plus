import axios from "axios";

// Exemplo de uso
// api.get<Team[]>("/teams")

// Team seria uma interface ou type do TypeScript.

// Se implementado corretamente no backend e 
// tipado corretamente no frontend, o exemplo acima
// retornaria uma lista de times. 

export const api = axios.create({
  baseURL: 'http://localhost:8080'
});