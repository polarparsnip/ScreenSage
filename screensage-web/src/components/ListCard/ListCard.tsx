/* eslint-disable @typescript-eslint/no-explicit-any */
import s from './ListCard.module.scss'
import { Link } from 'react-router-dom'

export function ListCard({ list }: { list: any }) {
  const date = new Date(list.createdAt);
  const formattedDate = date.toLocaleString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });

  return (
    <Link to={`/${list.watchlist ? 'watchlists' : 'lists'}/${list.id}`}>
      <div className={s.list_card}>
        {list.mediaListItems.length == 1 && 
          <img className={s.solo_image} src={list.mediaListItems[0].mediaImg} alt={'List image'} />
        }
        {list.mediaListItems.length > 1 && 
          <div className={s.image_stack}>
            <img className={s.image} src={list.mediaListItems[0].mediaImg} alt={'List image'} />
            <img className={s.image} src={list.mediaListItems[1].mediaImg} alt={'List image'} />
            {list.mediaListItems.length > 2 && <img className={s.image} src={list.mediaListItems[2].mediaImg} alt={'List image'} />}
          </div>
        }
        <div className={s.list_card__text_content}>
          <div>Created by: {list.user?.username ? list.user.username : 'Unknown'} on {formattedDate}</div>
          <h1>{list.title}</h1>
          <p>
            {list.description}
          </p>
          <div>{list.type.charAt(0).toUpperCase() + list.type.slice(1)} in list: {list.mediaListItems.length}</div>
        </div>
      </div>
    </Link>
  )
}
