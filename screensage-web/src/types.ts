export type Sort = {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export type Pageable = {
  offset: number;
  pageNumber: number;
  pageSize: number;
  paged: boolean;
  sort: Sort;
  unpaged: boolean;
}

export type Page = {
  content?: Array<Review>;
  empty: boolean;
  number: number;
  numberOfElements: number;
  size: number;
  totalElements: number;
  totalPages: number;
  sort: Sort;
  pageable: Pageable;
  start?: number;
  end?: number;
  first?: boolean;
  last?: boolean;
};

export type User = {
  id: number;
  username: string;
  // email: string;
  profileImg: string;
  passwordPlaceholder: string;
  // createdAt: string;
};

export type UserInfo = {
  token: string;
  expiresIn: number;
  user: User;
};

export type Review = {
  id: number;
  mediaId: number;
  user: User;
  rating: number;
  content: string;
  type: string;
  createdAt: Date;
}

export type MediaDetailed = {
  id: number;
  average_rating: number;
  user_rating: number;
  recent_reviews: Review[];
  title: string;
  original_title: string;
  original_language: string;
  overview: string;
  backdrop_path: string;
  poster_path: string;
  popularity: number;
  adult: boolean;
  belongs_to_collection: Collection;
  budget: number;
  genres: Genre[];
  homepage: string;
  // imdb_id: string;
  origin_country: string[];
  production_companies: ProductionCompany[];
  production_countries: ProductionCountry[];
  release_date: string;
  revenue: number;
  runtime: number;
  spoken_languages: SpokenLanguage[];
  status: string;
  tagline: string;
  video: boolean;
  videos: Videos;

  // vote_average: number;
  // vote_count: number;

  // tv variables
  name: string;
  original_name: string;
  first_air_date: string;
  number_of_episodes: number;
  number_of_seasons: number;
}

export type Media = {
  id: number;
  average_rating: number;
  user_rating: number;
  title: string;
  original_title: string;
  original_language: string;
  overview: string;
  adult: boolean;
  genre_ids: number[];
  popularity: number;
  backdrop_path: string;
  poster_path: string;
  release_date: string;
  video: boolean;

  // vote_average: number;
  // vote_count: number;

  // tv variables
  origin_country: string[] ;
  name: string;
  original_name: string;
  first_air_date: string;
}

export type MediaList = {
  results: Media[];
  page: number;
  total_pages: number;
  total_results: number;
  start?: number;
  end?: number;
}

export type Collection = {
  id: number;
  name: string;
  poster_path: string;
  backdrop_path: string; 
}

export type Genre = {
  id: number;
  name: number;
}

export type ProductionCompany = {
  id: number;
  logo_path: string;
  name: string;
  origin_country: string;
}


export type ProductionCountry = {
  iso_3166_1: string;
  name: string;
}

export type SpokenLanguage = {
  english_name: string;
  iso_639_1: string;
  name: string;
}

export type Video = {
  id: string;
  iso_639_1: string;
  iso_3166_1: string;
  name: string;
  key: string;
  site: string;
  size: number;
  type: string;
  official: boolean;
  published_at: string;
}

export type Videos = {
  results: Video[];
}

export type ChallengeOption = {
  id: number;
  option: string;
}

export type Challenge = {
  id: number;
  question: string;
  instructions: string;
  image:  string;
  type: string;
  options: ChallengeOption[];
}