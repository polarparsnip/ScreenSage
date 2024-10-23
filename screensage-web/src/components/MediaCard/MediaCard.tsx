import s from './MediaCard.module.scss';
import { Typography, Grow, Tooltip, Rating } from '@mui/material';
import { Link } from 'react-router-dom';
import { Media } from '../../types';


export function MediaCard({ media, type, i }: { media: Media, type: string, i: number}) {

  const rating = media?.user_rating 
  ? media?.user_rating 
  : media?.average_rating 
    ? media.average_rating 
    : null

  return (
    <div className={s.mediaCard}>
      <Grow in key={i} timeout={(i + 1) * 250}>
        <Link className={s.links} to={`/${type}/${media.id}`}>
          <img
            src={media.poster_path ? `https://image.tmdb.org/t/p/w500/${media.poster_path}` : 'https://www.fillmurray.com/200/300'}
            alt={(type == 'shows' || type == 'anime') ? media.name : media.title}
            className={s.mediaCard__image}
          />
          <Typography className={s.mediaCard__title} variant='h5'>{(type == 'shows' || type == 'anime') ? media.name : media.title}</Typography>
          <Tooltip disableTouchListener title={`${media.average_rating ? media.average_rating : 5.0} out of 5`}>
            <div className={s.mediaCard__rating}>
              {rating == null && <Typography component='legend'>No rating given</Typography>}
              <Rating readOnly value={rating} precision={0.5} />
            </div>
          </Tooltip>
        </Link>
      </Grow>
    </div>
  );
}
