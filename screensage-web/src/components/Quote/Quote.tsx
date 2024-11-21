import { Typography } from '@mui/material';
import s from './Quote.module.scss'

export function Quote({
  text,
  title,
  year,
  mediaId,
  type,
}: { 
  text: string;
  title: string;
  year: number;
  mediaId: number;
  type: string;
}) {
  return (
    <blockquote className={s.quote}>
      <Typography variant="h5" component="p" className={s.quoteText}>
        “{text}”
      </Typography>
      <Typography
        variant="subtitle2"
        component="a"
        href={`/${type}/${mediaId}`}
        className={s.quoteSource}
      >
        {title} ({year})
      </Typography>
    </blockquote>
  );
};

