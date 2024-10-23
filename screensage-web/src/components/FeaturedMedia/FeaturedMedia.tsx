import s from './FeaturedMedia.module.scss'
import { Box, Typography, Card, CardContent, CardMedia } from '@mui/material';
import { Link } from 'react-router-dom';
import { Media } from '../../types';


export function FeaturedMedia({ media, type }: { media: Media, type: string }) {

  if (!media) return null;

  return (
    <Box component={Link} to={`/${type}/${media.id}`} className={s.featuredMedia}>
      <Card className={s.featuredMedia__card} classes={{ root: s.cardRoot }}>
        <CardMedia
          image={`https://image.tmdb.org/t/p/original/${media?.backdrop_path}`}
          title={media.title}
          className={s.featuredMedia__card__media}
        />
        <Box padding='20px'>
          <CardContent className={s.featuredMedia__card__content} classes={{ root: s.cardContentRoot }}>
            <Typography variant='h5' gutterBottom>{media.title}</Typography>
            <Typography variant='body2'>{media.overview}</Typography>
          </CardContent>
        </Box>
      </Card>
    </Box>
  );
}
