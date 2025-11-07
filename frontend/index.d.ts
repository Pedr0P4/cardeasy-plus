// biome-ignore lint/correctness/noUnusedVariables: incorret warning
type Page<T> = {
  items: T[];
  page: number;
  lastPage: number;
  total: number;
};
