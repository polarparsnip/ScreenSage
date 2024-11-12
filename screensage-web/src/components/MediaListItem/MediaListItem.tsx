/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './MediaListItem.module.scss';
import { Typography, Grow, Tooltip } from '@mui/material';
import { Link } from 'react-router-dom';

export function MediaListItem({ 
  mediaListItem, 
  type, 
  author, 
  onRemove, 
  i 
}: { 
  mediaListItem: any, 
  type: string, 
  author: boolean, 
  onRemove: (id: number) => void, 
  i: number
}) {

  return (
    <div className={s.media_list_item}>
        <Grow in key={i} timeout={(i + 1) * 250}>
          <Tooltip disableTouchListener title={mediaListItem.mediaSummary}>
            <Link className={s.media_list_item__content} to={`/${type}/${mediaListItem.mediaId}`}>
      
              {author && 
              <Tooltip disableTouchListener title={'Remove from list'}>
                  <button 
                    className={s.media_list_item__remove} 
                    onClick={(event) => {
                      event.preventDefault();
                      onRemove(mediaListItem.id)
                    }}>
                    x
                  </button>
                </Tooltip>
              }
              <img
                src={mediaListItem.mediaImg}
                alt={mediaListItem.mediaTitle}
                className={s.media_list_item__image}
              />
              <div className={s.media_list_item__title_container}>
                <Typography className={s.media_list_item__title} variant='h5'>{mediaListItem.mediaTitle}</Typography>
              </div>
    
            </Link>
          </Tooltip>
        </Grow>
    </div>
  );
}
